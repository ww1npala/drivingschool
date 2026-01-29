package com.example.drivingschool.presentation;

import com.example.drivingschool.domain.services.EnrollmentService;
import com.example.drivingschool.domain.services.LessonService;
import com.example.drivingschool.domain.services.PaymentService;
import com.example.drivingschool.model.CoursePackage;
import com.example.drivingschool.model.DrivingLesson;
import com.example.drivingschool.model.Enrollment;
import com.example.drivingschool.model.EnrollmentRequest;
import com.example.drivingschool.model.LicenseCategory;
import com.example.drivingschool.model.Payment;
import com.example.drivingschool.model.Student;
import com.example.drivingschool.model.User;
import com.example.drivingschool.model.UserRole;
import com.example.drivingschool.presentation.format.PrintFormat;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainMenuView {

  private final User user;
  private final EnrollmentService enrollmentService;
  private final LessonService lessonService;
  private final PaymentService paymentService;

  private final Scanner scanner = new Scanner(System.in);

  // файл заявок у out/
  private static final Path APPLICATIONS_FILE = Path.of("out", "applications.json");

  public MainMenuView(User user,
      EnrollmentService enrollmentService,
      LessonService lessonService,
      PaymentService paymentService) {
    this.user = user;
    this.enrollmentService = enrollmentService;
    this.lessonService = lessonService;
    this.paymentService = paymentService;
  }

  public void start() {
    if (user.getRole() == UserRole.admin) {
      adminMenu();
    } else {
      userMenu();
    }
  }

  private void adminMenu() {
    while (true) {
      System.out.println("\n=== ADMIN MENU ===");
      System.out.println("1) Усі записи на навчання");
      System.out.println("2) Усі уроки водіння");
      System.out.println("3) Усі оплати");
      System.out.println("4) Заявки в автошколу");
      System.out.println("0) Вихід");
      System.out.print("> ");

      String choice = scanner.nextLine();

      switch (choice) {
        case "1":
          printEnrollments(enrollmentService.getAll());
          break;
        case "2":
          printLessons(lessonService.getAll());
          break;
        case "3":
          printPayments(paymentService.getAll());
          break;
        case "4":
          requestsAdminMenu();
          break;
        case "0":
          System.exit(0);
          return;
        default:
          System.out.println("Невірний вибір");
      }
    }
  }

  private void requestsAdminMenu() {
    while (true) {
      List<EnrollmentRequest> reqs = loadRequests();

      System.out.println("\n--- Заявки (" + reqs.size() + ") ---");
      printRequests(reqs);

      System.out.println("1) Схвалити заявку");
      System.out.println("2) Відхилити заявку");
      System.out.println("0) Назад");
      System.out.print("> ");

      String ch = scanner.nextLine();

      switch (ch) {
        case "1":
          approveRequest(reqs);
          break;
        case "2":
          rejectRequest(reqs);
          break;
        case "0":
          return;
        default:
          System.out.println("Невірний вибір");
      }
    }
  }

  private void approveRequest(List<EnrollmentRequest> reqs) {
    EnrollmentRequest r = pickRequestById(reqs);
    if (r == null) {
      return;
    }

    if (r.getStatus() != EnrollmentRequest.RequestStatus.NEW) {
      System.out.println("Заявка вже оброблена: " + r.getStatus());
      return;
    }

    long studentId = nextStudentId();
    Student s = new Student(studentId, r.getFullName(), "-", r.getEmail(), r.getBirthDate());

    long enrollmentId = nextEnrollmentId();

    // базовий пакет, 2 години
    CoursePackage basePackage = new CoursePackage(0, 2, BigDecimal.ONE);

    Enrollment e = new Enrollment(
        enrollmentId,
        s,
        r.getRequestedCategory(),
        basePackage,
        LocalDate.now().toString(),
        BigDecimal.ONE,
        2
    );

    try {
      enrollmentService.create(e);
      r.setStatus(EnrollmentRequest.RequestStatus.APPROVED);
      saveRequests(reqs);

      System.out.println("Ок. Заявку схвалено, Enrollment створено:");
      System.out.println(PrintFormat.enrollment(e));
    } catch (Exception ex) {
      System.out.println(ex.getMessage());
    }
  }

  private void rejectRequest(List<EnrollmentRequest> reqs) {
    EnrollmentRequest r = pickRequestById(reqs);
    if (r == null) {
      return;
    }

    if (r.getStatus() != EnrollmentRequest.RequestStatus.NEW) {
      System.out.println("Заявка вже оброблена: " + r.getStatus());
      return;
    }

    r.setStatus(EnrollmentRequest.RequestStatus.REJECTED);
    saveRequests(reqs);
    System.out.println("Ок. Заявку відхилено");
  }

  private EnrollmentRequest pickRequestById(List<EnrollmentRequest> reqs) {
    if (reqs == null || reqs.isEmpty()) {
      System.out.println("Немає заявок");
      return null;
    }

    System.out.print("Введіть ID заявки: ");
    long id;
    try {
      id = Long.parseLong(scanner.nextLine().trim());
    } catch (Exception e) {
      System.out.println("Невірний ID");
      return null;
    }

    for (int i = 0; i < reqs.size(); i++) {
      EnrollmentRequest r = reqs.get(i);
      if (r.getRequestId() == id) {
        return r;
      }
    }

    System.out.println("Заявку не знайдено");
    return null;
  }

  private void printRequests(List<EnrollmentRequest> reqs) {
    if (reqs == null || reqs.isEmpty()) {
      System.out.println("Немає заявок");
      return;
    }

    System.out.println("ID | Email | ПІБ | ДН | Категорія | Статус");
    for (int i = 0; i < reqs.size(); i++) {
      EnrollmentRequest r = reqs.get(i);
      String cat = r.getRequestedCategory() == null ? "-" : r.getRequestedCategory().getCode();
      System.out.println(
          r.getRequestId() + " | " + safe(r.getEmail()) + " | " + safe(r.getFullName()) +
              " | " + safe(r.getBirthDate()) + " | " + cat + " | " + r.getStatus()
      );
    }
  }

  private void userMenu() {
    while (true) {
      System.out.println("\n=== USER MENU ===");
      System.out.println("1) Подати заявку в автошколу");
      System.out.println("2) Статус моєї заявки");
      System.out.println("3) Мої записи на навчання");
      System.out.println("4) Мої уроки водіння");
      System.out.println("5) Мої оплати");
      System.out.println("0) Вихід");
      System.out.print("> ");

      String choice = scanner.nextLine();

      switch (choice) {
        case "1":
          submitRequest();
          break;
        case "2":
          printMyRequests();
          break;
        case "3":
          printEnrollments(filterMyEnrollments());
          break;
        case "4":
          printLessons(filterMyLessons());
          break;
        case "5":
          printPayments(filterMyPayments());
          break;
        case "0":
          System.exit(0);
          return;
        default:
          System.out.println("Невірний вибір");
      }
    }
  }

  private void submitRequest() {
    List<EnrollmentRequest> reqs = loadRequests();

    for (int i = 0; i < reqs.size(); i++) {
      EnrollmentRequest r = reqs.get(i);
      if (r.getEmail() != null && r.getEmail().equalsIgnoreCase(user.getEmail())) {
        if (r.getStatus() == EnrollmentRequest.RequestStatus.NEW) {
          System.out.println("У вас вже є активна заявка (NEW). Дочекайтесь рішення адміна.");
          return;
        }
      }
    }

    System.out.println("\n--- Подати заявку в автошколу ---");

    System.out.print("Введіть своє ПІБ: ");
    String fullName = scanner.nextLine().trim();
    if (fullName.isEmpty()) {
      System.out.println("ПІБ не може бути порожнім");
      return;
    }

    System.out.print("Введіть повну дату народження (dd.MM.yyyy): ");
    String birthInput = scanner.nextLine().trim();

    String birthIso;
    try {
      birthIso = toIsoDate(birthInput);
    } catch (Exception e) {
      System.out.println("Невірний формат дати. Приклад: 09.09.2006");
      return;
    }

    int age;
    try {
      age = calcAge(birthIso);
    } catch (Exception e) {
      System.out.println("Невірна дата народження");
      return;
    }

    List<LicenseCategory> available = new ArrayList<>();
    if (age >= 16) {
      available.add(LicenseCategory.categoryA(1));
    }
    if (age >= 18) {
      available.add(LicenseCategory.categoryB(2));
    }
    if (age >= 21) {
      available.add(LicenseCategory.categoryC(3));
    }

    if (available.isEmpty()) {
      System.out.println("Вік: " + age + ". Немає доступних категорій.");
      return;
    }

    System.out.println("Вік: " + age);
    System.out.println("Доступні категорії:");
    for (int i = 0; i < available.size(); i++) {
      System.out.println(
          (i + 1) + ") " + available.get(i).getCode() + " (" + available.get(i).getDescription()
              + ")"
      );
    }
    System.out.print("> ");

    int pick;
    try {
      pick = Integer.parseInt(scanner.nextLine().trim());
    } catch (Exception e) {
      System.out.println("Невірний вибір");
      return;
    }

    if (pick < 1 || pick > available.size()) {
      System.out.println("Невірний вибір");
      return;
    }

    LicenseCategory chosen = available.get(pick - 1);

    long requestId = nextRequestId(reqs);
    EnrollmentRequest req = new EnrollmentRequest(
        requestId,
        fullName,
        birthIso,
        user.getEmail(),
        chosen
    );

    reqs.add(req);
    saveRequests(reqs);

    System.out.println("Ок. Заявку надіслано. Статус: NEW");
  }

  private void printMyRequests() {
    List<EnrollmentRequest> reqs = loadRequests();

    boolean any = false;
    for (int i = 0; i < reqs.size(); i++) {
      EnrollmentRequest r = reqs.get(i);
      if (r.getEmail() != null && r.getEmail().equalsIgnoreCase(user.getEmail())) {
        any = true;
        String cat = r.getRequestedCategory() == null ? "-" : r.getRequestedCategory().getCode();
        System.out.println(
            "Заявка #" + r.getRequestId() + " | Категорія: " + cat + " | Статус: " + r.getStatus()
        );
      }
    }
    if (!any) {
      System.out.println("У вас ще немає заявок");
    }
  }

  private List<Enrollment> filterMyEnrollments() {
    List<Enrollment> all = enrollmentService.getAll();
    List<Enrollment> res = new ArrayList<>();
    for (int i = 0; i < all.size(); i++) {
      Enrollment e = all.get(i);
      if (e.getStudent() != null && e.getStudent().getEmail() != null) {
        if (e.getStudent().getEmail().equalsIgnoreCase(user.getEmail())) {
          res.add(e);
        }
      }
    }
    return res;
  }

  private List<DrivingLesson> filterMyLessons() {
    List<DrivingLesson> all = lessonService.getAll();
    List<DrivingLesson> res = new ArrayList<>();
    for (int i = 0; i < all.size(); i++) {
      DrivingLesson l = all.get(i);
      if (l.getEnrollment() != null && l.getEnrollment().getStudent() != null) {
        String em = l.getEnrollment().getStudent().getEmail();
        if (em != null && em.equalsIgnoreCase(user.getEmail())) {
          res.add(l);
        }
      }
    }
    return res;
  }

  private List<Payment> filterMyPayments() {
    List<Payment> all = paymentService.getAll();
    List<Payment> res = new ArrayList<>();
    for (int i = 0; i < all.size(); i++) {
      Payment p = all.get(i);
      if (p.getEnrollment() != null && p.getEnrollment().getStudent() != null) {
        String em = p.getEnrollment().getStudent().getEmail();
        if (em != null && em.equalsIgnoreCase(user.getEmail())) {
          res.add(p);
        }
      }
    }
    return res;
  }

  private void printEnrollments(List<Enrollment> list) {
    System.out.println("\n--- Записи на навчання (" + (list == null ? 0 : list.size()) + ") ---");
    if (list == null || list.isEmpty()) {
      System.out.println("Немає записів");
      return;
    }

    for (int i = 0; i < list.size(); i++) {
      System.out.println(PrintFormat.enrollment(list.get(i)));
      System.out.println();
    }
  }

  private void printLessons(List<DrivingLesson> list) {
    System.out.println("\n--- Уроки водіння (" + (list == null ? 0 : list.size()) + ") ---");
    if (list == null || list.isEmpty()) {
      System.out.println("Немає уроків");
      return;
    }

    for (int i = 0; i < list.size(); i++) {
      System.out.println(PrintFormat.lesson(list.get(i)));
      System.out.println();
    }
  }

  private void printPayments(List<Payment> list) {
    System.out.println("\n--- Оплати (" + (list == null ? 0 : list.size()) + ") ---");
    if (list == null || list.isEmpty()) {
      System.out.println("Немає оплат");
      return;
    }

    for (int i = 0; i < list.size(); i++) {
      System.out.println(PrintFormat.payment(list.get(i)));
      System.out.println();
    }
  }

  //json
  private List<EnrollmentRequest> loadRequests() {
    try {
      Path p = APPLICATIONS_FILE;
      if (!Files.exists(p)) {
        Files.writeString(p, "[]", StandardCharsets.UTF_8);
        return new ArrayList<>();
      }

      String json = Files.readString(p, StandardCharsets.UTF_8).trim();
      if (json.isEmpty() || json.equals("[]")) {
        return new ArrayList<>();
      }

      String body = json.trim();
      if (body.startsWith("[")) {
        body = body.substring(1);
      }
      if (body.endsWith("]")) {
        body = body.substring(0, body.length() - 1);
      }
      body = body.trim();
      if (body.isEmpty()) {
        return new ArrayList<>();
      }

      String[] objects = body.split("\\},\\s*\\{");

      List<EnrollmentRequest> res = new ArrayList<>();

      for (int i = 0; i < objects.length; i++) {
        String o = objects[i].trim();
        if (!o.startsWith("{")) {
          o = "{" + o;
        }
        if (!o.endsWith("}")) {
          o = o + "}";
        }

        long id = parseLong(o, "requestId", -1);
        String fullName = parseString(o, "fullName");
        String birthDate = parseString(o, "birthDate");
        String email = parseString(o, "email");
        String category = parseString(o, "category");
        String status = parseString(o, "status");

        LicenseCategory lc = null;
        if ("A".equalsIgnoreCase(category)) {
          lc = LicenseCategory.categoryA(1);
        }
        if ("B".equalsIgnoreCase(category)) {
          lc = LicenseCategory.categoryB(2);
        }
        if ("C".equalsIgnoreCase(category)) {
          lc = LicenseCategory.categoryC(3);
        }

        EnrollmentRequest r = new EnrollmentRequest(id, fullName, birthDate, email, lc);

        try {
          r.setStatus(EnrollmentRequest.RequestStatus.valueOf(status));
        } catch (Exception e) {
          r.setStatus(EnrollmentRequest.RequestStatus.NEW);
        }

        res.add(r);
      }

      return res;

    } catch (Exception e) {
      return new ArrayList<>();
    }
  }

  private void saveRequests(List<EnrollmentRequest> list) {
    StringBuilder sb = new StringBuilder();
    sb.append("[\n");

    for (int i = 0; i < list.size(); i++) {
      EnrollmentRequest r = list.get(i);

      String cat = r.getRequestedCategory() == null ? "" : r.getRequestedCategory().getCode();
      String status = r.getStatus() == null ? "NEW" : r.getStatus().name();

      sb.append("  {\n");
      sb.append("    \"requestId\": ").append(r.getRequestId()).append(",\n");
      sb.append("    \"fullName\": \"").append(jsonEscape(safeJson(r.getFullName())))
          .append("\",\n");
      sb.append("    \"birthDate\": \"").append(jsonEscape(safeJson(r.getBirthDate())))
          .append("\",\n");
      sb.append("    \"email\": \"").append(jsonEscape(safeJson(r.getEmail()))).append("\",\n");
      sb.append("    \"category\": \"").append(jsonEscape(cat)).append("\",\n");
      sb.append("    \"status\": \"").append(jsonEscape(status)).append("\"\n");
      sb.append("  }");

      if (i < list.size() - 1) {
        sb.append(",");
      }
      sb.append("\n");
    }

    sb.append("]\n");

    try {
      Files.writeString(APPLICATIONS_FILE, sb.toString(), StandardCharsets.UTF_8);
    } catch (Exception ignored) {
    }
  }

  private static String safeJson(String s) {
    return s == null ? "" : s;
  }

  private static String jsonEscape(String s) {
    return s.replace("\\", "\\\\").replace("\"", "\\\"");
  }

  private static long parseLong(String obj, String key, long def) {
    String k = "\"" + key + "\"";
    int i = obj.indexOf(k);
    if (i < 0) {
      return def;
    }

    int colon = obj.indexOf(":", i);
    if (colon < 0) {
      return def;
    }

    int j = colon + 1;
    while (j < obj.length() && (obj.charAt(j) == ' ')) {
      j++;
    }

    StringBuilder num = new StringBuilder();
    while (j < obj.length()) {
      char c = obj.charAt(j);
      if (c >= '0' && c <= '9') {
        num.append(c);
        j++;
      } else {
        break;
      }
    }

    try {
      return Long.parseLong(num.toString());
    } catch (Exception e) {
      return def;
    }
  }

  private static String parseString(String obj, String key) {
    String k = "\"" + key + "\"";
    int i = obj.indexOf(k);
    if (i < 0) {
      return "";
    }

    int colon = obj.indexOf(":", i);
    if (colon < 0) {
      return "";
    }

    int firstQuote = obj.indexOf("\"", colon + 1);
    if (firstQuote < 0) {
      return "";
    }

    int secondQuote = firstQuote + 1;
    boolean esc = false;
    StringBuilder sb = new StringBuilder();
    while (secondQuote < obj.length()) {
      char c = obj.charAt(secondQuote);
      if (esc) {
        sb.append(c);
        esc = false;
      } else {
        if (c == '\\') {
          esc = true;
        } else if (c == '"') {
          break;
        } else {
          sb.append(c);
        }
      }
      secondQuote++;
    }

    return sb.toString();
  }

  private static String safe(String s) {
    if (s == null) {
      return "-";
    }
    String t = s.trim();
    return t.isEmpty() ? "-" : t;
  }

  private static String toIsoDate(String ddmmyyyy) {
    String[] parts = ddmmyyyy.split("\\.");
    if (parts.length != 3) {
      throw new IllegalArgumentException("bad date");
    }
    String dd = parts[0].trim();
    String mm = parts[1].trim();
    String yyyy = parts[2].trim();
    if (dd.length() == 1) {
      dd = "0" + dd;
    }
    if (mm.length() == 1) {
      mm = "0" + mm;
    }
    return yyyy + "-" + mm + "-" + dd;
  }

  private static int calcAge(String isoDate) {
    LocalDate birth = LocalDate.parse(isoDate);
    LocalDate now = LocalDate.now();
    return Period.between(birth, now).getYears();
  }

  private static long nextRequestId(List<EnrollmentRequest> reqs) {
    long m = 0;
    for (int i = 0; i < reqs.size(); i++) {
      long id = reqs.get(i).getRequestId();
      if (id > m) {
        m = id;
      }
    }
    return m + 1;
  }

  private long nextEnrollmentId() {
    List<Enrollment> all = enrollmentService.getAll();
    long m = 0;
    for (int i = 0; i < all.size(); i++) {
      long id = all.get(i).getEnrollmentId();
      if (id > m) {
        m = id;
      }
    }
    return m + 1;
  }

  private long nextStudentId() {
    List<Enrollment> all = enrollmentService.getAll();
    long m = 0;
    for (int i = 0; i < all.size(); i++) {
      Student s = all.get(i).getStudent();
      if (s != null && s.getStudentId() > m) {
        m = s.getStudentId();
      }
    }
    return m + 1;
  }
}
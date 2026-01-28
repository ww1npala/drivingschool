package com.example.drivingschool;

import com.example.drivingschool.generator.DemoDataGenerator;
import com.example.drivingschool.model.DrivingLesson;
import com.example.drivingschool.model.Enrollment;
import com.example.drivingschool.model.LessonStatus;
import com.example.drivingschool.model.Payment;
import com.example.drivingschool.model.PaymentMethod;
import com.example.drivingschool.model.Student;
import com.example.drivingschool.storage.JsonFileStorage;
import com.example.drivingschool.validation.ModelValidator;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

  private static final DateTimeFormatter DT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

  public static void main(String[] args) {

    // генерація демо даних автошколи
    List<Enrollment> enrollments = DemoDataGenerator.generateEnrollments(20);
    List<DrivingLesson> lessons = DemoDataGenerator.generateLessons(enrollments, 30);
    List<Payment> payments = DemoDataGenerator.generatePayments(enrollments, 15);

    // валідація даних
    for (int i = 0; i < enrollments.size(); i++) {
      ModelValidator.validateEnrollment(enrollments.get(i));
    }
    for (int i = 0; i < lessons.size(); i++) {
      ModelValidator.validateLesson(lessons.get(i));
    }
    for (int i = 0; i < payments.size(); i++) {
      ModelValidator.validatePayment(payments.get(i));
    }

    // збереження в json
    Path enrollmentsFile = Path.of("out", "enrollments.json");
    Path lessonsFile = Path.of("out", "lessons.json");
    Path paymentsFile = Path.of("out", "payments.json");

    JsonFileStorage.saveList(enrollmentsFile, enrollments);
    JsonFileStorage.saveList(lessonsFile, lessons);
    JsonFileStorage.saveList(paymentsFile, payments);

    List<Enrollment> loadedEnrollments = JsonFileStorage.loadList(enrollmentsFile,
        Enrollment.class);
    List<DrivingLesson> loadedLessons = JsonFileStorage.loadList(lessonsFile, DrivingLesson.class);
    List<Payment> loadedPayments = JsonFileStorage.loadList(paymentsFile, Payment.class);

    System.out.println("Система управління автошколою: демонстрація даних");
    System.out.println("  Записів на навчання: " + loadedEnrollments.size());
    System.out.println("  Уроків водіння:      " + loadedLessons.size());
    System.out.println("  Оплат:              " + loadedPayments.size());

    /**
     ====================3 etap=================
     **/

    Map<Long, Enrollment> enrollmentRepo = new HashMap<>();
    Map<Long, DrivingLesson> lessonRepo = new HashMap<>();
    Map<Long, Payment> paymentRepo = new HashMap<>();

    // заповнюємо репозиторії
    for (int i = 0; i < loadedEnrollments.size(); i++) {
      Enrollment e = loadedEnrollments.get(i);
      enrollmentRepo.put(e.getEnrollmentId(), e);
    }
    for (int i = 0; i < loadedLessons.size(); i++) {
      DrivingLesson l = loadedLessons.get(i);
      lessonRepo.put(l.getLessonId(), l);
    }
    for (int i = 0; i < loadedPayments.size(); i++) {
      Payment p = loadedPayments.get(i);
      paymentRepo.put(p.getPaymentId(), p);
    }

    System.out.println();
    System.out.println("CRUD / пошук / фільтрація: перевірка (Етап 3)");

    long anyEnrollmentId = loadedEnrollments.get(0).getEnrollmentId();

    Enrollment foundEnrollment = enrollmentRepo.get(anyEnrollmentId);
    System.out.println(
        "READ Enrollment #" + anyEnrollmentId + ": " + foundEnrollment.getStudent().getFullName());

    // оновимо телефон студенту
    String oldPhone = foundEnrollment.getStudent().getPhone();
    foundEnrollment.getStudent().setPhone("+380001112233");
    ModelValidator.validateEnrollment(foundEnrollment);
    enrollmentRepo.put(foundEnrollment.getEnrollmentId(), foundEnrollment);
    System.out.println("UPDATE Enrollment #" + anyEnrollmentId + ": phone " + oldPhone + " -> "
        + foundEnrollment.getStudent().getPhone());

    // додамо новий запис
    long newEnrollmentId = maxEnrollmentId(enrollmentRepo) + 1L;
    Student newStudent = new Student(
        maxStudentId(enrollmentRepo) + 1L,
        "Новий Учень",
        "+380991234567",
        "new.student@example.com",
        "2000-05-10"
    );

    Enrollment newEnrollment = new Enrollment(
        newEnrollmentId,
        newStudent,
        loadedEnrollments.get(0).getLicenseCategory(),
        loadedEnrollments.get(0).getCoursePackage(),
        "2026-01-27",
        loadedEnrollments.get(0).getCoursePackage().getPrice(),
        loadedEnrollments.get(0).getCoursePackage().getTotalHours()
    );

    ModelValidator.validateEnrollment(newEnrollment);
    enrollmentRepo.put(newEnrollment.getEnrollmentId(), newEnrollment);
    System.out.println(
        "CREATE Enrollment #" + newEnrollmentId + ": " + newEnrollment.getStudent().getFullName());

    enrollmentRepo.remove(newEnrollmentId);
    System.out.println("DELETE Enrollment #" + newEnrollmentId + ": видалено");

    long anyLessonId = loadedLessons.get(0).getLessonId();

    DrivingLesson foundLesson = lessonRepo.get(anyLessonId);
    System.out.println("READ Lesson #" + anyLessonId + ": status=" + foundLesson.getStatus());

    // оновлюємо (змінимо статус на COMPLETED)
    LessonStatus oldStatus = foundLesson.getStatus();
    foundLesson.setStatus(LessonStatus.COMPLETED);
    ModelValidator.validateLesson(foundLesson);
    lessonRepo.put(foundLesson.getLessonId(), foundLesson);
    System.out.println("UPDATE Lesson #" + anyLessonId + ": status " + oldStatus + " -> "
        + foundLesson.getStatus());

    // створюємо (додамо новий урок до існуючого enrollment)
    long newLessonId = maxLessonId(lessonRepo) + 1L;
    DrivingLesson newLesson = new DrivingLesson(
        newLessonId,
        foundEnrollment,
        foundLesson.getInstructor(),
        foundLesson.getCar(),
        LocalDateTime.now().plusDays(2).format(DT),
        1.5,
        LessonStatus.PLANNED
    );
    ModelValidator.validateLesson(newLesson);
    lessonRepo.put(newLesson.getLessonId(), newLesson);
    System.out.println(
        "CREATE Lesson #" + newLessonId + ": enrollmentId=" + newLesson.getEnrollment()
            .getEnrollmentId());

    // видалення
    lessonRepo.remove(newLessonId);
    System.out.println("DELETE Lesson #" + newLessonId + ": видалено");

    long anyPaymentId = loadedPayments.get(0).getPaymentId();

    Payment foundPayment = paymentRepo.get(anyPaymentId);
    System.out.println("READ Payment #" + anyPaymentId + ": amount=" + foundPayment.getAmount());

    // зміна методу оплати
    PaymentMethod oldMethod = foundPayment.getMethod();
    foundPayment.setMethod(PaymentMethod.CARD);
    ModelValidator.validatePayment(foundPayment);
    paymentRepo.put(foundPayment.getPaymentId(), foundPayment);
    System.out.println("UPDATE Payment #" + anyPaymentId + ": method " + oldMethod + " -> "
        + foundPayment.getMethod());

    // створення
    long newPaymentId = maxPaymentId(paymentRepo) + 1L;
    Payment newPayment = new Payment(
        newPaymentId,
        foundEnrollment,
        BigDecimal.valueOf(500),
        LocalDateTime.now().format(DT),
        PaymentMethod.CASH
    );
    ModelValidator.validatePayment(newPayment);
    paymentRepo.put(newPayment.getPaymentId(), newPayment);
    System.out.println(
        "CREATE Payment #" + newPaymentId + ": enrollmentId=" + newPayment.getEnrollment()
            .getEnrollmentId());

    // видалення
    paymentRepo.remove(newPaymentId);
    System.out.println("DELETE Payment #" + newPaymentId + ": видалено");

    /**
     Пошук і фільтрація
     **/

    // фільтр уроків за їх статусом
    int planned = 0;
    int completed = 0;
    int canceled = 0;
    for (DrivingLesson l : lessonRepo.values()) {
      if (l.getStatus() == LessonStatus.PLANNED) {
        planned++;
      } else if (l.getStatus() == LessonStatus.COMPLETED) {
        completed++;
      } else if (l.getStatus() == LessonStatus.CANCELED) {
        canceled++;
      }
    }

    System.out.println();
    System.out.println("Фільтрація уроків за статусами:");
    System.out.println("  Заплановано: " + planned);
    System.out.println("  Проведено:   " + completed);
    System.out.println("  Скасовано:   " + canceled);

    // пошук записів за категорією
    String searchCategory = "B";
    int byCategory = 0;
    for (Enrollment e : enrollmentRepo.values()) {
      if (e.getLicenseCategory() != null && searchCategory.equalsIgnoreCase(
          e.getLicenseCategory().getCode())) {
        byCategory++;
      }
    }
    System.out.println();
    System.out.println("Пошук записів за категорією '" + searchCategory + "': " + byCategory);

    // фільтрація оплат, сума в діапазоні
    BigDecimal min = BigDecimal.valueOf(300);
    BigDecimal max = BigDecimal.valueOf(2000);
    int paymentsInRange = 0;
    BigDecimal sumInRange = BigDecimal.ZERO;

    for (Payment p : paymentRepo.values()) {
      BigDecimal a = p.getAmount();
      if (a.compareTo(min) >= 0 && a.compareTo(max) <= 0) {
        paymentsInRange++;
        sumInRange = sumInRange.add(a);
      }
    }

    System.out.println();
    System.out.println(
        "Фільтрація оплат (сума " + min + " .. " + max + " грн): " + paymentsInRange + " шт, разом "
            + sumInRange + " грн");

    // пошук топ по сумі оплат
    Map<Long, BigDecimal> paidByEnrollment = new HashMap<>();
    BigDecimal totalPaid = BigDecimal.ZERO;

    for (Payment p : paymentRepo.values()) {
      totalPaid = totalPaid.add(p.getAmount());
      long enrollmentId = p.getEnrollment().getEnrollmentId();

      BigDecimal current = paidByEnrollment.get(enrollmentId);
      if (current == null) {
        current = BigDecimal.ZERO;
      }

      paidByEnrollment.put(enrollmentId, current.add(p.getAmount()));
    }

    long bestEnrollmentId = -1;
    BigDecimal bestAmount = BigDecimal.ZERO;
    for (Map.Entry<Long, BigDecimal> entry : paidByEnrollment.entrySet()) {
      if (entry.getValue().compareTo(bestAmount) > 0) {
        bestAmount = entry.getValue();
        bestEnrollmentId = entry.getKey();
      }
    }

    System.out.println();
    System.out.println("Фінанси:");
    System.out.println("  Загальна сума оплат: " + totalPaid + " грн");
    if (bestEnrollmentId != -1) {
      System.out.println(
          "  Найбільше оплат по запису #" + bestEnrollmentId + ": " + bestAmount + " грн");
    }
  }

  private static long maxEnrollmentId(Map<Long, Enrollment> repo) {
    long max = 0;
    for (Long id : repo.keySet()) {
      if (id > max) {
        max = id;
      }
    }
    return max;
  }

  private static long maxLessonId(Map<Long, DrivingLesson> repo) {
    long max = 0;
    for (Long id : repo.keySet()) {
      if (id > max) {
        max = id;
      }
    }
    return max;
  }

  private static long maxPaymentId(Map<Long, Payment> repo) {
    long max = 0;
    for (Long id : repo.keySet()) {
      if (id > max) {
        max = id;
      }
    }
    return max;
  }

  private static long maxStudentId(Map<Long, Enrollment> repo) {
    long max = 0;
    for (Enrollment e : repo.values()) {
      if (e.getStudent() != null && e.getStudent().getStudentId() > max) {
        max = e.getStudent().getStudentId();
      }
    }
    return max;
  }
}
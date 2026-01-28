package com.example.drivingschool;

import com.example.drivingschool.auth.AuthService;
import com.example.drivingschool.auth.ConsoleEmailSender;
import com.example.drivingschool.auth.EmailSender;
import com.example.drivingschool.domain.services.EnrollmentService;
import com.example.drivingschool.domain.services.LessonService;
import com.example.drivingschool.domain.services.PaymentService;
import com.example.drivingschool.domain.uow.InMemoryUnitOfWork;
import com.example.drivingschool.domain.uow.UnitOfWork;
import com.example.drivingschool.generator.DemoDataGenerator;
import com.example.drivingschool.model.DrivingLesson;
import com.example.drivingschool.model.Enrollment;
import com.example.drivingschool.model.LessonStatus;
import com.example.drivingschool.model.Payment;
import com.example.drivingschool.model.PaymentMethod;
import com.example.drivingschool.model.Student;
import com.example.drivingschool.model.User;
import com.example.drivingschool.presentation.AuthView;
import com.example.drivingschool.presentation.MainMenuView;
import com.example.drivingschool.repository.entities.DrivingLessonRepository;
import com.example.drivingschool.repository.entities.EnrollmentRepository;
import com.example.drivingschool.repository.entities.PaymentRepository;
import com.example.drivingschool.repository.entities.UserRepository;
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

    LessonStatus oldStatus = foundLesson.getStatus();
    foundLesson.setStatus(LessonStatus.COMPLETED);
    ModelValidator.validateLesson(foundLesson);
    lessonRepo.put(foundLesson.getLessonId(), foundLesson);
    System.out.println("UPDATE Lesson #" + anyLessonId + ": status " + oldStatus + " -> "
        + foundLesson.getStatus());

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

    lessonRepo.remove(newLessonId);
    System.out.println("DELETE Lesson #" + newLessonId + ": видалено");

    long anyPaymentId = loadedPayments.get(0).getPaymentId();

    Payment foundPayment = paymentRepo.get(anyPaymentId);
    System.out.println("READ Payment #" + anyPaymentId + ": amount=" + foundPayment.getAmount());

    PaymentMethod oldMethod = foundPayment.getMethod();
    foundPayment.setMethod(PaymentMethod.CARD);
    ModelValidator.validatePayment(foundPayment);
    paymentRepo.put(foundPayment.getPaymentId(), foundPayment);
    System.out.println("UPDATE Payment #" + anyPaymentId + ": method " + oldMethod + " -> "
        + foundPayment.getMethod());

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

    paymentRepo.remove(newPaymentId);
    System.out.println("DELETE Payment #" + newPaymentId + ": видалено");

    // auth + ui
    System.out.println();
    System.out.println("===== AUTH =====");

    Path usersFile = Path.of("out", "users.json");
    UserRepository userRepository = new UserRepository(usersFile);

    EmailSender emailSender = new ConsoleEmailSender(); // потім замінимо на Gmail SMTP
    AuthService authService = new AuthService(userRepository, emailSender);

    AuthView authView = new AuthView(authService);
    User currentUser = authView.start();

    // domian service
    EnrollmentRepository enrollmentRepository = new EnrollmentRepository(enrollmentsFile);
    DrivingLessonRepository drivingLessonRepository = new DrivingLessonRepository(lessonsFile);
    PaymentRepository paymentRepository = new PaymentRepository(paymentsFile);

    UnitOfWork uow = new InMemoryUnitOfWork(enrollmentRepository, drivingLessonRepository,
        paymentRepository);

    EnrollmentService enrollmentService = new EnrollmentService(uow);
    LessonService lessonService = new LessonService(uow);
    PaymentService paymentService = new PaymentService(uow);

    MainMenuView menuView = new MainMenuView(
        currentUser,
        enrollmentService,
        lessonService,
        paymentService
    );
    menuView.start();
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
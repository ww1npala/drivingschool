package com.example.drivingschool;

import com.example.drivingschool.auth.AuthService;
import com.example.drivingschool.auth.BCryptPasswordHasher;
import com.example.drivingschool.auth.EmailSender;
import com.example.drivingschool.auth.PasswordHasher;
import com.example.drivingschool.auth.SmtpEmailSender;
import com.example.drivingschool.domain.services.EnrollmentService;
import com.example.drivingschool.domain.services.LessonService;
import com.example.drivingschool.domain.services.PaymentService;
import com.example.drivingschool.domain.uow.InMemoryUnitOfWork;
import com.example.drivingschool.domain.uow.UnitOfWork;
import com.example.drivingschool.generator.DemoDataGenerator;
import com.example.drivingschool.model.DrivingLesson;
import com.example.drivingschool.model.Enrollment;
import com.example.drivingschool.model.Payment;
import com.example.drivingschool.model.User;
import com.example.drivingschool.presentation.AuthView;
import com.example.drivingschool.presentation.MainMenuView;
import com.example.drivingschool.repository.entities.DrivingLessonRepository;
import com.example.drivingschool.repository.entities.EnrollmentRepository;
import com.example.drivingschool.repository.entities.PaymentRepository;
import com.example.drivingschool.repository.entities.UserRepository;
import com.example.drivingschool.storage.JsonFileStorage;
import com.example.drivingschool.validation.ModelValidator;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Main {

  public static void main(String[] args) {

    // якщо нема створюємо out/
    try {
      Files.createDirectories(Path.of("out"));
    } catch (Exception ignored) {
    }
    
    try {
      Path appsFile = Path.of("out", "applications.json");
      if (!Files.exists(appsFile)) {
        Files.writeString(appsFile, "[]", StandardCharsets.UTF_8);
      }
    } catch (Exception ignored) {
    }

    List<Enrollment> enrollments = DemoDataGenerator.generateEnrollments(20);
    List<DrivingLesson> lessons = DemoDataGenerator.generateLessons(enrollments, 30);
    List<Payment> payments = DemoDataGenerator.generatePayments(enrollments, 15);

    for (int i = 0; i < enrollments.size(); i++) {
      ModelValidator.validateEnrollment(enrollments.get(i));
    }
    for (int i = 0; i < lessons.size(); i++) {
      ModelValidator.validateLesson(lessons.get(i));
    }
    for (int i = 0; i < payments.size(); i++) {
      ModelValidator.validatePayment(payments.get(i));
    }

    Path enrollmentsFile = Path.of("out", "enrollments.json");
    Path lessonsFile = Path.of("out", "lessons.json");
    Path paymentsFile = Path.of("out", "payments.json");

    JsonFileStorage.saveList(enrollmentsFile, enrollments);
    JsonFileStorage.saveList(lessonsFile, lessons);
    JsonFileStorage.saveList(paymentsFile, payments);

    // uow + services
    EnrollmentRepository enrollmentRepo = new EnrollmentRepository(enrollmentsFile);
    DrivingLessonRepository lessonRepo = new DrivingLessonRepository(lessonsFile);
    PaymentRepository paymentRepo = new PaymentRepository(paymentsFile);

    UnitOfWork uow = new InMemoryUnitOfWork(enrollmentRepo, lessonRepo, paymentRepo);

    EnrollmentService enrollmentService = new EnrollmentService(uow);
    LessonService lessonService = new LessonService(uow);
    PaymentService paymentService = new PaymentService(uow);

    //auth
    Path usersFile = Path.of("out", "users.json");
    UserRepository userRepository = new UserRepository(usersFile);

    PasswordHasher hasher = new BCryptPasswordHasher();
    String gmailAppPassword = "nfzbjsblgfloktwy";

    EmailSender emailSender = new SmtpEmailSender(
        "drivingschool.np@gmail.com",
        gmailAppPassword
    );

    AuthService authService = new AuthService(userRepository, hasher, emailSender);

    AuthView authView = new AuthView(authService);
    User currentUser = authView.start();

    // menu
    MainMenuView menuView = new MainMenuView(
        currentUser,
        enrollmentService,
        lessonService,
        paymentService
    );
    menuView.start();
  }
}
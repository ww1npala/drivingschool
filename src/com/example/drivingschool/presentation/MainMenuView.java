package com.example.drivingschool.presentation;

import com.example.drivingschool.domain.services.EnrollmentService;
import com.example.drivingschool.domain.services.LessonService;
import com.example.drivingschool.domain.services.PaymentService;
import com.example.drivingschool.model.User;
import com.example.drivingschool.model.UserRole;
import java.util.Scanner;

public class MainMenuView {

  private final User user;
  private final EnrollmentService enrollmentService;
  private final LessonService lessonService;
  private final PaymentService paymentService;

  private final Scanner scanner = new Scanner(System.in);

  public MainMenuView(
      User user,
      EnrollmentService enrollmentService,
      LessonService lessonService,
      PaymentService paymentService
  ) {
    this.user = user;
    this.enrollmentService = enrollmentService;
    this.lessonService = lessonService;
    this.paymentService = paymentService;
  }

  public void start() {
    if (user.getRole() == UserRole.ADMIN) {
      adminMenu();
    } else {
      userMenu();
    }
  }

  private void adminMenu() {
    while (true) {
      System.out.println("\n=== ADMIN МЕНЮ ===");
      System.out.println("1) Переглянути всі записи");
      System.out.println("2) Переглянути всі уроки");
      System.out.println("3) Переглянути всі оплати");
      System.out.println("0) Вихід");
      System.out.print("> ");

      String choice = scanner.nextLine();

      switch (choice) {
        case "1":
          System.out.println("[ADMIN] Тут буде список Enrollment");
          break;
        case "2":
          System.out.println("[ADMIN] Тут буде список DrivingLesson");
          break;
        case "3":
          System.out.println("[ADMIN] Тут буде список Payment");
          break;
        case "0":
          System.exit(0);
        default:
          System.out.println("Невірний вибір");
      }
    }
  }

  private void userMenu() {
    while (true) {
      System.out.println("\n=== USER МЕНЮ ===");
      System.out.println("1) Мої записи на навчання");
      System.out.println("2) Мої уроки");
      System.out.println("3) Мої оплати");
      System.out.println("0) Вихід");
      System.out.print("> ");

      String choice = scanner.nextLine();

      switch (choice) {
        case "1":
          System.out.println("[USER] Тут будуть ТІЛЬКИ свої Enrollment");
          break;
        case "2":
          System.out.println("[USER] Тут будуть ТІЛЬКИ свої DrivingLesson");
          break;
        case "3":
          System.out.println("[USER] Тут будуть ТІЛЬКИ свої Payment");
          break;
        case "0":
          System.exit(0);
        default:
          System.out.println("Невірний вибір");
      }
    }
  }
}
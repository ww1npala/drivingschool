package com.example.drivingschool.presentation;

import com.example.drivingschool.domain.services.EnrollmentService;
import com.example.drivingschool.domain.services.LessonService;
import com.example.drivingschool.domain.services.PaymentService;
import com.example.drivingschool.model.DrivingLesson;
import com.example.drivingschool.model.Enrollment;
import com.example.drivingschool.model.Payment;
import com.example.drivingschool.model.User;
import com.example.drivingschool.model.UserRole;
import com.example.drivingschool.presentation.format.PrintFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainMenuView {

  private final User user;
  private final EnrollmentService enrollmentService;
  private final LessonService lessonService;
  private final PaymentService paymentService;

  private final Scanner scanner = new Scanner(System.in);

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
    if (user.getRole() == UserRole.ADMIN) {
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
        case "0":
          System.exit(0);
          return;
        default:
          System.out.println("Невірний вибір");
      }
    }
  }

  private void userMenu() {
    while (true) {
      System.out.println("\n=== USER MENU ===");
      System.out.println("1) Мої записи на навчання");
      System.out.println("2) Мої уроки водіння");
      System.out.println("3) Мої оплати");
      System.out.println("0) Вихід");
      System.out.print("> ");

      String choice = scanner.nextLine();

      switch (choice) {
        case "1":
          printEnrollments(filterMyEnrollments());
          break;
        case "2":
          printLessons(filterMyLessons());
          break;
        case "3":
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
}
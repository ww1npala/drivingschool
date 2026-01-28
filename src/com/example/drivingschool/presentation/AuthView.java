package com.example.drivingschool.presentation;

import com.example.drivingschool.auth.AuthService;
import com.example.drivingschool.model.User;
import com.example.drivingschool.model.UserRole;
import java.util.Scanner;

public final class AuthView {

  private final AuthService auth;
  private final Scanner sc = new Scanner(System.in);

  public AuthView(AuthService auth) {
    this.auth = auth;
  }

  public User start() {
    while (true) {
      System.out.println("\n=== AUTH ===");
      System.out.println("1) Login");
      System.out.println("2) Register");
      System.out.print("> ");
      String choice = sc.nextLine();

      try {
        if ("1".equals(choice)) {
          return loginFlow();
        } else if ("2".equals(choice)) {
          return registerFlow();
        } else {
          System.out.println("Невірний вибір");
        }
      } catch (Exception ex) {
        System.out.println("Помилка: " + ex.getMessage());
      }
    }
  }

  private User registerFlow() {
    System.out.print("Введіть ваш Email: ");
    String email = sc.nextLine();

    auth.beginEmailVerification(email);
    System.out.println("Код відправлено на пошту.");

    while (true) {
      System.out.print("Введіть код (Дійсний 2 хвилини) або 'r' щоб надіслати ще раз: ");
      String code = sc.nextLine();

      if ("r".equalsIgnoreCase(code.trim())) {
        auth.resendCode(email);
        System.out.println("Новий код відправлено.");
        continue;
      }

      boolean ok = auth.verifyEmailCode(email, code);
      if (!ok) {
        System.out.println("Код невірний або прострочений. Введи знову або натисни 'r'.");
        continue;
      }

      // код вірний, далі login + пароль
      System.out.print("Введіть бажаний Login: ");
      String login = sc.nextLine();

      System.out.print("Введіть пароль: ");
      String pass1 = sc.nextLine();

      System.out.print("Підтвердіть пароль: ");
      String pass2 = sc.nextLine();

      if (!pass1.equals(pass2)) {
        System.out.println("Паролі не співпадають. Спробуй ще раз.");
        continue;
      }

      UserRole role = UserRole.USER;

      User created = auth.registerAfterCode(login, email, pass1, role);
      System.out.println("Реєстрація успішна. Вітаю, " + created.getLogin() + "!");
      return created;
    }
  }

  private User loginFlow() {
    System.out.print("Введіть логін: ");
    String login = sc.nextLine();

    System.out.print("Введіть пароль: ");
    String password = sc.nextLine();

    User u = auth.login(login, password);
    System.out.println("Успішний вхід. Привіт, " + u.getLogin() + "!");
    return u;
  }
}
package com.example.drivingschool.presentation;

import com.example.drivingschool.auth.AuthService;
import com.example.drivingschool.model.User;
import java.util.Scanner;

public class AuthView {

  private final AuthService authService;
  private final Scanner scanner = new Scanner(System.in);

  public AuthView(AuthService authService) {
    this.authService = authService;
  }

  public User start() {
    while (true) {
      System.out.println("\n=== АВТОШКОЛА ===");
      System.out.println("1) Реєстрація");
      System.out.println("2) Вхід");
      System.out.println("0) Вихід");
      System.out.print("> ");

      String choice = scanner.nextLine();

      try {
        switch (choice) {
          case "1":
            register();
            break;
          case "2":
            return login();
          case "0":
            System.exit(0);
          default:
            System.out.println("Невірний вибір");
        }
      } catch (Exception ex) {
        System.out.println("Помилка: " + ex.getMessage());
      }
    }
  }

  private void register() {
    System.out.print("Email: ");
    String email = scanner.nextLine();

    System.out.print("Пароль: ");
    String password = scanner.nextLine();

    authService.startRegistration(email, password);

    System.out.print("Введіть код з пошти: ");
    String code = scanner.nextLine();

    authService.confirmCode(email, code, password);

    System.out.println("Реєстрація успішна. Можете увійти.");
  }

  private User login() {
    System.out.print("Email: ");
    String email = scanner.nextLine();

    System.out.print("Пароль: ");
    String password = scanner.nextLine();

    User user = authService.login(email, password);
    System.out.println("Вхід виконано успішно");
    return user;
  }
}
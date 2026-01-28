package com.example.drivingschool.auth;

import java.util.regex.Pattern;

public final class AuthValidator {

  private AuthValidator() {
  }

  // латиниця + цифри+._%+- до @, домен, .крапка, 2+ літери
  private static final Pattern EMAIL =
      Pattern.compile("^[A-Za-z0-9._%+-]{6,}@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

  // login 3-20, латиниця/цифри/_
  private static final Pattern LOGIN =
      Pattern.compile("^[A-Za-z0-9_]{3,20}$");

  public static void validateEmail(String email) {
    if (email == null || email.isBlank()) {
      throw new IllegalArgumentException("Email не може бути пустим");
    }
    String e = email.trim();
    if (!EMAIL.matcher(e).matches()) {
      throw new IllegalArgumentException("Невірний email. Приклад: username@gmail.com");
    }
  }

  public static void validateLogin(String login) {
    if (login == null || login.isBlank()) {
      throw new IllegalArgumentException("Login не може бути пустим");
    }
    String l = login.trim();
    if (!LOGIN.matcher(l).matches()) {
      throw new IllegalArgumentException("Login: 3-20 символів, тільки латиниця/цифри/_");
    }
  }

  public static void validatePassword(String password) {
    if (password == null) {
      throw new IllegalArgumentException("Пароль не може бути пустим");
    }
    if (password.length() < 6) {
      throw new IllegalArgumentException("Пароль має бути мінімум 6 символів");
    }
  }
}
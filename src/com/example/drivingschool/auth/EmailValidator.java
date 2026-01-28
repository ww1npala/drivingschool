package com.example.drivingschool.auth;

public final class EmailValidator {

  private EmailValidator() {
  }

  // валідація емейлу, мінімум 6 символів в назві емейлу, @, домен з крапкою
  public static void validate(String email) {
    if (email == null) {
      throw new IllegalArgumentException("Email обов'язковий");
    }

    String e = email.trim();
    if (e.isEmpty()) {
      throw new IllegalArgumentException("Email обов'язковий");
    }

    for (int i = 0; i < e.length(); i++) {
      if (e.charAt(i) > 127) {
        throw new IllegalArgumentException("Email має бути англійською");
      }
    }

    String regex = "^[A-Za-z0-9._%+-]{6,}@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    if (!e.matches(regex)) {
      throw new IllegalArgumentException("Некоректний email (приклад: name123@gmail.com)");
    }
  }
}

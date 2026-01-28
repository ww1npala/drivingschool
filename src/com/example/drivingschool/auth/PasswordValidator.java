package com.example.drivingschool.auth;

public final class PasswordValidator {

  private PasswordValidator() {
  }

  // валідація, мінімум 6 символів, без пробілів
  public static void validate(String password) {
    if (password == null) {
      throw new IllegalArgumentException("Пароль обов'язковий");
    }

    String p = password;
    if (p.trim().isEmpty()) {
      throw new IllegalArgumentException("Пароль обов'язковий");
    }
    if (p.length() < 6) {
      throw new IllegalArgumentException("Пароль має бути мінімум 6 символів");
    }
    if (!p.equals(p.trim())) {
      throw new IllegalArgumentException("Пароль не повинен містити пробіли на початку/в кінці");
    }

    for (int i = 0; i < p.length(); i++) {
      char c = p.charAt(i);
      if (c < 33 || c > 126) {
        throw new IllegalArgumentException("Пароль має містити тільки видимі ASCII символи");
      }
    }
  }
}

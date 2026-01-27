package com.example.drivingschool.validation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

public final class ValidationUtils {

  private ValidationUtils() {
  }

  // перевірки
  public static void notNull(Object value, String fieldName) {
    if (value == null) {
      throw new ValidationException(fieldName + " не може бути порожнім");
    }
  }

  public static void notBlank(String value, String fieldName) {
    if (value == null || value.trim().isEmpty()) {
      throw new ValidationException(fieldName + " не може бути порожнім");
    }
  }

  public static void positiveLong(long value, String fieldName) {
    if (value <= 0) {
      throw new ValidationException(fieldName + " повинно бути більше 0");
    }
  }

  public static void positiveInt(int value, String fieldName) {
    if (value <= 0) {
      throw new ValidationException(fieldName + " повинно бути більше 0");
    }
  }

  public static void nonNegativeInt(int value, String fieldName) {
    if (value < 0) {
      throw new ValidationException(fieldName + " не може бути від’ємним");
    }
  }

  public static void positiveDouble(double value, String fieldName) {
    if (value <= 0) {
      throw new ValidationException(fieldName + " повинно бути більше 0");
    }
  }

  public static void positiveMoney(BigDecimal value, String fieldName) {
    notNull(value, fieldName);
    if (value.compareTo(BigDecimal.ZERO) <= 0) {
      throw new ValidationException(fieldName + " повинно бути більше 0");
    }
  }

  // біз логіка, вік

  // формат дати yyyy-MM-dd
  public static int ageFromBirthDate(String birthDate) {
    notBlank(birthDate, "Дата народження");

    try {
      LocalDate date = LocalDate.parse(birthDate);
      return Period.between(date, LocalDate.now()).getYears();
    } catch (Exception e) {
      throw new ValidationException(
          "Невірний формат дати народження (yyyy-MM-dd): " + birthDate
      );
    }
  }

  // мін вік для категорії прав
  public static int minAgeForCategory(String categoryCode) {
    notBlank(categoryCode, "Категорія прав");

    return switch (categoryCode.toUpperCase()) {
      case "A" -> 16;
      case "B" -> 18;
      case "C" -> 21;
      default -> throw new ValidationException(
          "Невідома категорія прав: " + categoryCode
      );
    };
  }
}
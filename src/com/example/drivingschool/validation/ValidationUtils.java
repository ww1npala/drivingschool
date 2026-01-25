package com.example.drivingschool.validation;

import java.math.BigDecimal;

public final class ValidationUtils {

  private ValidationUtils() {
  }

  public static void notNull(Object value, String fieldName) {
    if (value == null) {
      throw new ValidationException(fieldName + " must not be null");
    }
  }

  public static void notBlank(String value, String fieldName) {
    if (value == null || value.trim().isEmpty()) {
      throw new ValidationException(fieldName + " must not be blank");
    }
  }

  public static void positiveLong(long value, String fieldName) {
    if (value <= 0) {
      throw new ValidationException(fieldName + " must be > 0");
    }
  }

  public static void positiveInt(int value, String fieldName) {
    if (value <= 0) {
      throw new ValidationException(fieldName + " must be > 0");
    }
  }

  public static void nonNegativeInt(int value, String fieldName) {
    if (value < 0) {
      throw new ValidationException(fieldName + " must be >= 0");
    }
  }

  public static void positiveDouble(double value, String fieldName) {
    if (value <= 0) {
      throw new ValidationException(fieldName + " must be > 0");
    }
  }

  public static void positiveMoney(BigDecimal value, String fieldName) {
    notNull(value, fieldName);
    if (value.compareTo(BigDecimal.ZERO) <= 0) {
      throw new ValidationException(fieldName + " must be > 0");
    }
  }
}
package com.example.drivingschool.validation;

import com.example.drivingschool.model.DrivingLesson;
import com.example.drivingschool.model.Enrollment;
import com.example.drivingschool.model.Payment;
import com.example.drivingschool.model.Student;

public final class ModelValidator {

  private ModelValidator() {
  }

  // student
  public static void validateStudent(Student s) {
    ValidationUtils.notNull(s, "student");

    ValidationUtils.positiveLong(s.getStudentId(), "studentId");
    ValidationUtils.notBlank(s.getFullName(), "ПІБ");
    ValidationUtils.notBlank(s.getPhone(), "телефон");
    ValidationUtils.notBlank(s.getEmail(), "email");
    ValidationUtils.notBlank(s.getBirthDate(), "дата народження");

    int age = ValidationUtils.ageFromBirthDate(s.getBirthDate());
    if (age < 14 || age > 90) {
      throw new ValidationException(
          "Вік студента поза допустимими межами: " + age
      );
    }
  }

  // enrollment
  public static void validateEnrollment(Enrollment e) {
    ValidationUtils.notNull(e, "enrollment");

    ValidationUtils.positiveLong(e.getEnrollmentId(), "enrollmentId");

    ValidationUtils.notNull(e.getStudent(), "student");
    validateStudent(e.getStudent());

    ValidationUtils.notNull(e.getLicenseCategory(), "категорія прав");

    ValidationUtils.notNull(e.getCoursePackage(), "пакет курсу");
    if (e.getCoursePackage().getTotalHours() <= 0) {
      throw new ValidationException("Пакет курсу повинен мати більше 0 годин");
    }

    ValidationUtils.notBlank(e.getStartDate(), "дата початку");
    ValidationUtils.positiveMoney(e.getAgreedPrice(), "ціна курсу");
    ValidationUtils.nonNegativeInt(e.getRemainingHours(), "залишок годин");

    // залишок годин не може перевищувати години пакету
    int totalHours = e.getCoursePackage().getTotalHours();
    if (e.getRemainingHours() > totalHours) {
      throw new ValidationException(
          "Залишок годин (" + e.getRemainingHours() +
              ") перевищує години пакету (" + totalHours + ")"
      );
    }

    // біз логіка
    int studentAge = ValidationUtils.ageFromBirthDate(
        e.getStudent().getBirthDate()
    );

    if (!e.getLicenseCategory().isAgeAllowed(studentAge)) {
      throw new ValidationException(
          "Вік студента (" + studentAge + " років) "
              + "не відповідає вимогам категорії "
              + e.getLicenseCategory().getCode()
              + " (мінімум "
              + e.getLicenseCategory().getMinAge() + " років)"
      );
    }
  }

  // driving lesson
  public static void validateLesson(DrivingLesson l) {
    ValidationUtils.notNull(l, "урок");

    ValidationUtils.positiveLong(l.getLessonId(), "lessonId");

    ValidationUtils.notNull(l.getEnrollment(), "enrollment");
    validateEnrollment(l.getEnrollment());

    ValidationUtils.notNull(l.getInstructor(), "інструктор");
    ValidationUtils.notNull(l.getCar(), "авто");

    ValidationUtils.notBlank(l.getStartDateTime(), "дата та час уроку");
    ValidationUtils.positiveDouble(l.getDurationHours(), "тривалість уроку");
    ValidationUtils.notNull(l.getStatus(), "статус уроку");

    double duration = l.getDurationHours();
    int remaining = l.getEnrollment().getRemainingHours();

    if (duration > remaining) {
      throw new ValidationException(
          "Тривалість уроку (" + duration + " год) "
              + "перевищує доступні години (" + remaining + ")"
      );
    }
  }

  //payment
  public static void validatePayment(Payment p) {
    ValidationUtils.notNull(p, "платіж");

    ValidationUtils.positiveLong(p.getPaymentId(), "paymentId");

    ValidationUtils.notNull(p.getEnrollment(), "enrollment");
    validateEnrollment(p.getEnrollment());

    ValidationUtils.positiveMoney(p.getAmount(), "сума платежу");
    ValidationUtils.notBlank(p.getPaymentDateTime(), "дата платежу");
    ValidationUtils.notNull(p.getMethod(), "метод оплати");
  }
}
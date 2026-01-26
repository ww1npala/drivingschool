package com.example.drivingschool.validation;

import com.example.drivingschool.model.DrivingLesson;
import com.example.drivingschool.model.Enrollment;
import com.example.drivingschool.model.Payment;
import com.example.drivingschool.model.Student;

public final class ModelValidator {

  private ModelValidator() {
  }

  public static void validateStudent(Student s) {
    ValidationUtils.notNull(s, "student");
  }

  public static void validateEnrollment(Enrollment e) {
    ValidationUtils.notNull(e, "enrollment");
    ValidationUtils.positiveLong(e.getEnrollmentId(), "enrollmentId");
    ValidationUtils.notNull(e.getStudent(), "student");
    ValidationUtils.notNull(e.getLicenseCategory(), "licenseCategory");
    ValidationUtils.notNull(e.getCoursePackage(), "coursePackage");
    ValidationUtils.notBlank(e.getStartDate(), "startDate");
    ValidationUtils.positiveMoney(e.getAgreedPrice(), "agreedPrice");
    ValidationUtils.nonNegativeInt(e.getRemainingHours(), "remainingHours");
  }

  public static void validateLesson(DrivingLesson l) {
    ValidationUtils.notNull(l, "lesson");
    ValidationUtils.positiveLong(l.getLessonId(), "lessonId");
    ValidationUtils.notNull(l.getEnrollment(), "enrollment");
    ValidationUtils.notNull(l.getInstructor(), "instructor");
    ValidationUtils.notNull(l.getCar(), "car");
    ValidationUtils.notBlank(l.getStartDateTime(), "startDateTime");
    ValidationUtils.positiveDouble(l.getDurationHours(), "durationHours");
    ValidationUtils.notNull(l.getStatus(), "status");
  }

  public static void validatePayment(Payment p) {
    ValidationUtils.notNull(p, "payment");
    ValidationUtils.positiveLong(p.getPaymentId(), "paymentId");
    ValidationUtils.notNull(p.getEnrollment(), "enrollment");
    ValidationUtils.positiveMoney(p.getAmount(), "amount");
    ValidationUtils.notBlank(p.getPaymentDateTime(), "paymentDateTime");
    ValidationUtils.notNull(p.getMethod(), "method");
  }
}
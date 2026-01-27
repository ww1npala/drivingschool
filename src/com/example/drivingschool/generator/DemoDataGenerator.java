package com.example.drivingschool.generator;

import com.example.drivingschool.model.Car;
import com.example.drivingschool.model.CoursePackage;
import com.example.drivingschool.model.DrivingLesson;
import com.example.drivingschool.model.Enrollment;
import com.example.drivingschool.model.Instructor;
import com.example.drivingschool.model.LessonStatus;
import com.example.drivingschool.model.LicenseCategory;
import com.example.drivingschool.model.Payment;
import com.example.drivingschool.model.PaymentMethod;
import com.example.drivingschool.model.Student;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import net.datafaker.Faker;

public final class DemoDataGenerator {

  private static final Faker FAKER = new Faker(java.util.Locale.ENGLISH);

  private DemoDataGenerator() {
  }

  // enrollments
  public static List<Enrollment> generateEnrollments(int count) {
    if (count < 0) {
      throw new IllegalArgumentException("count must be >= 0");
    }

    List<Enrollment> list = new ArrayList<>();

    for (int i = 0; i < count; i++) {
      long studentId = i + 1L;
      long enrollmentId = i + 1L;

      String fullName = FAKER.name().fullName();

      // не використовуємо FAKER.phoneNumber(), щоб не ловити libphonenumber помилку
      String phone = "+380" + FAKER.number().digits(9);

      String email = FAKER.internet().emailAddress();

      // 1980..2007 => вік буде адекватний
      String birthDate = randomDate(LocalDate.of(1980, 1, 1), LocalDate.of(2007, 12, 31));
      Student student = new Student(studentId, fullName, phone, email, birthDate);

      int age = com.example.drivingschool.validation.ValidationUtils.ageFromBirthDate(
          student.getBirthDate());
      LicenseCategory category = randomLicenseCategoryByAge(age);
      CoursePackage coursePackage = randomCoursePackage();

      String startDate = randomDate(LocalDate.now().minusMonths(2), LocalDate.now().plusMonths(1));

      BigDecimal agreedPrice = coursePackage.getPrice();

      // робимо так, щоб remainingHours не вибивав валідацію
      int remainingHours = ThreadLocalRandom.current()
          .nextInt(1, coursePackage.getTotalHours() + 1);

      Enrollment e = new Enrollment(
          enrollmentId,
          student,
          category,
          coursePackage,
          startDate,
          agreedPrice,
          remainingHours
      );

      list.add(e);
    }

    return list;
  }

  // lessons
  public static List<DrivingLesson> generateLessons(List<Enrollment> enrollments, int count) {
    if (count < 0) {
      throw new IllegalArgumentException("count must be >= 0");
    }

    List<DrivingLesson> list = new ArrayList<>();
    if (enrollments == null || enrollments.isEmpty()) {
      return list;
    }

    for (int i = 0; i < count; i++) {
      long lessonId = i + 1L;

      Enrollment e = enrollments.get(ThreadLocalRandom.current().nextInt(enrollments.size()));

      Instructor instructor = randomInstructor(i + 1L);
      Car car = randomCar(i + 1L);

      LocalDateTime dt = LocalDateTime.now().minusDays(ThreadLocalRandom.current().nextInt(0, 30));
      String startDateTime = dt.toString(); // yyyy-MM-ddTHH:mm:ss

      // тривалість робимо так, щоб не перевищувало remainingHours
      double durationHours = randomLessonDuration(e.getRemainingHours());

      LessonStatus status = randomLessonStatus();

      DrivingLesson lesson = new DrivingLesson(
          lessonId,
          e,
          instructor,
          car,
          startDateTime,
          durationHours,
          status
      );

      // якщо урок завершений
      if (status == LessonStatus.COMPLETED) {
        lesson.complete();
      }

      // якщо відміна, просто ставимо cancel (нічого не списує)
      if (status == LessonStatus.CANCELED) {
        lesson.cancel();
      }

      list.add(lesson);
    }

    return list;
  }

  private static double randomLessonDuration(int remainingHours) {
    // min 1 hour
    int max = Math.max(1, remainingHours);
    // щоб не вибивало валідацію беремо vid 1 do max
    return ThreadLocalRandom.current().nextInt(1, max + 1);
  }

  private static LessonStatus randomLessonStatus() {
    // частіше planned, інколи completed , canceled
    int r = ThreadLocalRandom.current().nextInt(100);
    if (r < 60) {
      return LessonStatus.PLANNED;
    }
    if (r < 90) {
      return LessonStatus.COMPLETED;
    }
    return LessonStatus.CANCELED;
  }

  private static Instructor randomInstructor(long id) {
    String fullName = FAKER.name().fullName();
    String phone = "+380" + FAKER.number().digits(9);
    return new Instructor(id, fullName, phone);
  }

  private static Car randomCar(long id) {
    String plateNumber =
        "AA" + ThreadLocalRandom.current().nextInt(1000, 9999) + "BB";
    return new Car(id, plateNumber);
  }

  private static String randomFrom(String... items) {
    int idx = ThreadLocalRandom.current().nextInt(items.length);
    return items[idx];
  }

  private static LicenseCategory randomLicenseCategoryByAge(int age) {
    LicenseCategory[] categories = new LicenseCategory[]{
        new LicenseCategory(1L, "A", 16, "Motorcycles"),
        new LicenseCategory(2L, "B", 18, "Cars"),
        new LicenseCategory(3L, "C", 21, "Trucks"),
        new LicenseCategory(4L, "D", 24, "Buses")
    };

    List<LicenseCategory> allowed = new ArrayList<>();
    for (int i = 0; i < categories.length; i++) {
      if (age >= categories[i].getMinAge()) {
        allowed.add(categories[i]);
      }
    }

    // якщо age <16 (не має такого бути), то віддамо найменшу
    if (allowed.isEmpty()) {
      return categories[0];
    }

    int idx = ThreadLocalRandom.current().nextInt(allowed.size());
    return allowed.get(idx);
  }

  // payment
  public static List<Payment> generatePayments(List<Enrollment> enrollments, int count) {
    if (count < 0) {
      throw new IllegalArgumentException("count must be >= 0");
    }

    List<Payment> list = new ArrayList<>();
    if (enrollments == null || enrollments.isEmpty()) {
      return list;
    }

    for (int i = 0; i < count; i++) {
      long paymentId = i + 1L;

      Enrollment e = enrollments.get(ThreadLocalRandom.current().nextInt(enrollments.size()));

      // сума від 200 до 5000
      BigDecimal amount = BigDecimal.valueOf(ThreadLocalRandom.current().nextInt(200, 5001));

      String paymentDateTime = LocalDateTime.now()
          .minusDays(ThreadLocalRandom.current().nextInt(0, 30))
          .toString(); // yyyy-MM-ddTHH:mm:ss

      PaymentMethod method = randomPaymentMethod();

      Payment p = new Payment(paymentId, e, amount, paymentDateTime, method);
      list.add(p);
    }

    return list;
  }

  private static PaymentMethod randomPaymentMethod() {
    PaymentMethod[] values = PaymentMethod.values();
    return values[ThreadLocalRandom.current().nextInt(values.length)];
  }

  // helpers
  private static LicenseCategory randomLicenseCategory() {
    LicenseCategory[] categories = new LicenseCategory[]{
        new LicenseCategory(1L, "A", 16, "Motorcycles"),
        new LicenseCategory(2L, "B", 18, "Cars"),
        new LicenseCategory(3L, "C", 21, "Trucks"),
        new LicenseCategory(4L, "D", 24, "Buses")
    };

    int idx = ThreadLocalRandom.current().nextInt(categories.length);
    return categories[idx];
  }

  private static CoursePackage randomCoursePackage() {
    CoursePackage[] packages = new CoursePackage[]{
        new CoursePackage(1L, 10, BigDecimal.valueOf(8000)),
        new CoursePackage(2L, 20, BigDecimal.valueOf(14000)),
        new CoursePackage(3L, 30, BigDecimal.valueOf(20000)),
        new CoursePackage(4L, 40, BigDecimal.valueOf(25000))
    };

    int idx = ThreadLocalRandom.current().nextInt(packages.length);
    return packages[idx];
  }

  private static String randomDate(LocalDate from, LocalDate to) {
    long start = from.toEpochDay();
    long end = to.toEpochDay();
    long randomDay = ThreadLocalRandom.current().nextLong(start, end + 1);
    return LocalDate.ofEpochDay(randomDay).toString(); // yyyy-MM-dd
  }
}
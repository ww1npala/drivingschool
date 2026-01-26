package com.example.drivingschool.generator;

import com.example.drivingschool.model.CoursePackage;
import com.example.drivingschool.model.Enrollment;
import com.example.drivingschool.model.LicenseCategory;
import com.example.drivingschool.model.Student;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import net.datafaker.Faker;

public final class DemoDataGenerator {

  private static final Faker FAKER = new Faker(java.util.Locale.ENGLISH);

  private DemoDataGenerator() {
  }

  public static List<Enrollment> generateEnrollments(int count) {
    if (count < 0) {
      throw new IllegalArgumentException("count must be >= 0");
    }

    List<Enrollment> list = new ArrayList<>();

    for (int i = 0; i < count; i++) {
      long studentId = i + 1L;
      long enrollmentId = i + 1L;

      String fullName = FAKER.name().fullName();
      String phone = FAKER.phoneNumber().cellPhone();
      String email = FAKER.internet().emailAddress();

      String birthDate = randomDate(LocalDate.of(1980, 1, 1), LocalDate.of(2007, 12, 31));
      Student student = new Student(studentId, fullName, phone, email, birthDate);

      LicenseCategory category = randomLicenseCategory();
      CoursePackage coursePackage = randomCoursePackage();

      String startDate = randomDate(LocalDate.now().minusMonths(2), LocalDate.now().plusMonths(1));

      // можна брати ціну пакету чм робити домовлену окремо:

      BigDecimal agreedPrice = coursePackage.getPrice();
      int remainingHours = ThreadLocalRandom.current()
          .nextInt(0, coursePackage.getTotalHours() + 1);

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
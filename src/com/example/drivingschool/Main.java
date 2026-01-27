package com.example.drivingschool;

import com.example.drivingschool.generator.DemoDataGenerator;
import com.example.drivingschool.model.DrivingLesson;
import com.example.drivingschool.model.Enrollment;
import com.example.drivingschool.model.LessonStatus;
import com.example.drivingschool.model.Payment;
import com.example.drivingschool.storage.JsonFileStorage;
import com.example.drivingschool.validation.ModelValidator;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

  public static void main(String[] args) {

    // 1) Генеруємо дані автошколи
    List<Enrollment> enrollments = DemoDataGenerator.generateEnrollments(20);
    List<DrivingLesson> lessons = DemoDataGenerator.generateLessons(enrollments, 30);
    List<Payment> payments = DemoDataGenerator.generatePayments(enrollments, 15);

    // 2) Валідація даних
    for (int i = 0; i < enrollments.size(); i++) {
      ModelValidator.validateEnrollment(enrollments.get(i));
    }
    for (int i = 0; i < lessons.size(); i++) {
      ModelValidator.validateLesson(lessons.get(i));
    }
    for (int i = 0; i < payments.size(); i++) {
      ModelValidator.validatePayment(payments.get(i));
    }

    // 3) Зберігаємо в JSON
    Path enrollmentsFile = Path.of("out", "enrollments.json");
    Path lessonsFile = Path.of("out", "lessons.json");
    Path paymentsFile = Path.of("out", "payments.json");

    JsonFileStorage.saveList(enrollmentsFile, enrollments);
    JsonFileStorage.saveList(lessonsFile, lessons);
    JsonFileStorage.saveList(paymentsFile, payments);

    // 4) Читаємо назад (імітація "запуску програми наступного разу")
    List<Enrollment> loadedEnrollments = JsonFileStorage.loadList(enrollmentsFile,
        Enrollment.class);
    List<DrivingLesson> loadedLessons = JsonFileStorage.loadList(lessonsFile, DrivingLesson.class);
    List<Payment> loadedPayments = JsonFileStorage.loadList(paymentsFile, Payment.class);

    // 5) Демонстрація роботи системи автошколи (звіт)
    System.out.println("Система управління автошколою: демонстраційні дані");
    System.out.println("Записів на навчання: " + loadedEnrollments.size());
    System.out.println("Уроків водіння: " + loadedLessons.size());
    System.out.println("Оплат: " + loadedPayments.size());

    // 5.1 Статистика уроків по статусах
    int planned = 0;
    int completed = 0;
    int canceled = 0;

    for (int i = 0; i < loadedLessons.size(); i++) {
      LessonStatus st = loadedLessons.get(i).getStatus();
      if (st == LessonStatus.PLANNED) {
        planned++;
      } else if (st == LessonStatus.COMPLETED) {
        completed++;
      } else if (st == LessonStatus.CANCELED) {
        canceled++;
      }
    }

    System.out.println();
    System.out.println("Статуси уроків:");
    System.out.println("  Заплановано: " + planned);
    System.out.println("  Проведено:   " + completed);
    System.out.println("  Скасовано:   " + canceled);

    // 5.2 Сума оплат загалом + сума оплат по кожному запису (Enrollment)
    BigDecimal totalPaid = BigDecimal.ZERO;
    Map<Long, BigDecimal> paidByEnrollment = new HashMap<>();

    for (int i = 0; i < loadedPayments.size(); i++) {
      Payment p = loadedPayments.get(i);
      totalPaid = totalPaid.add(p.getAmount());

      long enrollmentId = p.getEnrollment().getEnrollmentId();
      BigDecimal current = paidByEnrollment.get(enrollmentId);
      if (current == null) {
        current = BigDecimal.ZERO;
      }
      paidByEnrollment.put(enrollmentId, current.add(p.getAmount()));
    }

    System.out.println();
    System.out.println("Фінанси:");
    System.out.println("  Загальна сума оплат: " + totalPaid + " грн");
    System.out.println("  Кількість записів з оплатами: " + paidByEnrollment.size());

    // 5.3 Знайти "топ-запис" за сумою оплат
    long bestEnrollmentId = -1;
    BigDecimal bestAmount = BigDecimal.ZERO;

    for (Map.Entry<Long, BigDecimal> entry : paidByEnrollment.entrySet()) {
      if (entry.getValue().compareTo(bestAmount) > 0) {
        bestAmount = entry.getValue();
        bestEnrollmentId = entry.getKey();
      }
    }

    if (bestEnrollmentId != -1) {
      System.out.println(
          "  Найбільше оплат по запису #" + bestEnrollmentId + ": " + bestAmount + " грн");
    }

    // 5.4 Приклад одного запису/уроку/оплати
    if (!loadedEnrollments.isEmpty()) {
      Enrollment e = loadedEnrollments.get(0);
      System.out.println();
      System.out.println("Приклад запису на навчання:");
      System.out.println("  Enrollment #" + e.getEnrollmentId()
          + ", учень: " + e.getStudent().getFullName()
          + ", категорія: " + e.getLicenseCategory().getCode()
          + ", пакет: " + e.getCoursePackage().getTotalHours() + " год"
          + ", залишок годин: " + e.getRemainingHours());
    }

    if (!loadedLessons.isEmpty()) {
      DrivingLesson l = loadedLessons.get(0);
      System.out.println();
      System.out.println("Приклад уроку водіння:");
      System.out.println("  Lesson #" + l.getLessonId()
          + ", enrollmentId: " + l.getEnrollment().getEnrollmentId()
          + ", інструктор: " + l.getInstructor()
          + ", авто: " + l.getCar()
          + ", тривалість: " + l.getDurationHours()
          + ", статус: " + l.getStatus());
    }

    if (!loadedPayments.isEmpty()) {
      Payment p = loadedPayments.get(0);
      System.out.println();
      System.out.println("Приклад оплати:");
      System.out.println("  Payment #" + p.getPaymentId()
          + ", enrollmentId: " + p.getEnrollment().getEnrollmentId()
          + ", сума: " + p.getAmount()
          + ", метод: " + p.getMethod()
          + ", дата/час: " + p.getPaymentDateTime());
    }
  }
}
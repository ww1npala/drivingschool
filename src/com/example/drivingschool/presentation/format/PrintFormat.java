package com.example.drivingschool.presentation.format;

import com.example.drivingschool.model.DrivingLesson;
import com.example.drivingschool.model.Enrollment;
import com.example.drivingschool.model.Payment;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.function.Supplier;

public final class PrintFormat {

  private PrintFormat() {
  }

  private static final String NL = System.lineSeparator();
  private static final DecimalFormat HOURS_FMT = new DecimalFormat("0.##");

  private static String safeStr(Supplier<String> s) {
    try {
      String v = s.get();
      return (v == null || v.trim().isEmpty()) ? "-" : v.trim();
    } catch (Exception e) {
      return "-";
    }
  }

  private static long safeLong(Supplier<Long> s) {
    try {
      Long v = s.get();
      return v == null ? -1 : v;
    } catch (Exception e) {
      return -1;
    }
  }

  private static BigDecimal safeMoney(Supplier<BigDecimal> s) {
    try {
      BigDecimal v = s.get();
      return v == null ? BigDecimal.ZERO : v;
    } catch (Exception e) {
      return BigDecimal.ZERO;
    }
  }

  private static String money(BigDecimal v) {
    if (v == null) {
      return "0";
    }
    return v.stripTrailingZeros().toPlainString();
  }

  private static String hours(double v) {
    if (v <= 0) {
      return "0";
    }
    return HOURS_FMT.format(v);
  }

  private static String line(String title) {
    return "----- " + title + " -----";
  }

  public static String enrollment(Enrollment e) {
    if (e == null) {
      return "Запис: -";
    }

    long id = e.getEnrollmentId();

    String student = safeStr(() -> e.getStudent().getFullName());
    String phone = safeStr(() -> e.getStudent().getPhone());
    String email = safeStr(() -> e.getStudent().getEmail());

    String category = safeStr(() -> e.getLicenseCategory().getCode());
    String pkg = String.format(
        "ID:%d, %d год, %s грн",
        safeLong(() -> e.getCoursePackage().getPackageId()),
        e.getCoursePackage() == null ? 0 : e.getCoursePackage().getTotalHours(),
        money(safeMoney(() -> e.getCoursePackage().getPrice()))
    );

    String start = safeStr(e::getStartDate);
    BigDecimal price = safeMoney(e::getAgreedPrice);
    int remaining = e.getRemainingHours();

    return line("Запис #" + id) + NL +
        "Учень: " + student + NL +
        "Тел: " + phone + NL +
        "Email: " + email + NL +
        "Категорія: " + category + NL +
        "Пакет: " + pkg + NL +
        "Старт: " + start + NL +
        "Сума: " + money(price) + " грн" + NL +
        "Залишок годин: " + remaining;
  }

  public static String lesson(DrivingLesson l) {
    if (l == null) {
      return "Урок: -";
    }

    long id = l.getLessonId();

    long enrollmentId = safeLong(() -> l.getEnrollment().getEnrollmentId());
    String student = safeStr(() -> l.getEnrollment().getStudent().getFullName());

    String instructor = safeStr(() -> l.getInstructor().getFullName());
    String car = safeStr(() -> l.getCar().getPlateNumber());

    String dateTime = safeStr(l::getStartDateTime);
    String status = String.valueOf(l.getStatus());

    String dur = hours(l.getDurationHours());
    String consumed = l.isHoursConsumed() ? "так" : "ні";

    return line("Урок #" + id) + NL +
        "Запис: #" + enrollmentId + NL +
        "Учень: " + student + NL +
        "Інструктор: " + instructor + NL +
        "Авто: " + car + NL +
        "Дата/час: " + dateTime + NL +
        "Тривалість: " + dur + " год" + NL +
        "Статус: " + status + NL +
        "Години списані: " + consumed;
  }

  public static String payment(Payment p) {
    if (p == null) {
      return "Оплата: -";
    }

    long id = p.getPaymentId();

    long enrollmentId = safeLong(() -> p.getEnrollment().getEnrollmentId());
    String student = safeStr(() -> p.getEnrollment().getStudent().getFullName());

    BigDecimal amount = safeMoney(p::getAmount);
    String dateTime = safeStr(p::getPaymentDateTime);
    String method = String.valueOf(p.getMethod());

    return line("Оплата #" + id) + NL +
        "Запис: #" + enrollmentId + NL +
        "Учень: " + student + NL +
        "Сума: " + money(amount) + " грн" + NL +
        "Дата/час: " + dateTime + NL +
        "Метод: " + method;
  }
}
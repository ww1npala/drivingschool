package com.example.drivingschool.model;

import java.math.BigDecimal;

public class Payment {

  private long paymentId;

  private Enrollment enrollment;

  private BigDecimal amount;
  private String paymentDateTime; // yyyy-MM-ddTHH:mm:ss (LocalDateTime.toString())
  private PaymentMethod method;

  public Payment(long paymentId,
      Enrollment enrollment,
      BigDecimal amount,
      String paymentDateTime,
      PaymentMethod method) {

    this.paymentId = paymentId;
    this.enrollment = enrollment;
    this.amount = amount;
    this.paymentDateTime = paymentDateTime;
    this.method = method;
  }


  public boolean isPositiveAmount() {
    return amount != null && amount.signum() > 0;
  }

  public boolean isSameEnrollment(Enrollment other) {
    if (enrollment == null || other == null) {
      return false;
    }
    return enrollment.getEnrollmentId() == other.getEnrollmentId();
  }


  // геттери сеттери
  public long getPaymentId() {
    return paymentId;
  }

  public void setPaymentId(long paymentId) {
    this.paymentId = paymentId;
  }

  public Enrollment getEnrollment() {
    return enrollment;
  }

  public void setEnrollment(Enrollment enrollment) {
    this.enrollment = enrollment;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public String getPaymentDateTime() {
    return paymentDateTime;
  }

  public void setPaymentDateTime(String paymentDateTime) {
    this.paymentDateTime = paymentDateTime;
  }

  public PaymentMethod getMethod() {
    return method;
  }

  public void setMethod(PaymentMethod method) {
    this.method = method;
  }

  @Override
  public String toString() {
    return "Payment{" +
        "paymentId=" + paymentId +
        ", enrollmentId=" + (enrollment == null ? null : enrollment.getEnrollmentId()) +
        ", amount=" + amount +
        ", paymentDateTime='" + paymentDateTime + '\'' +
        ", method=" + method +
        '}';
  }
}
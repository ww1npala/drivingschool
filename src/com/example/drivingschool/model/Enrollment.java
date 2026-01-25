package com.example.drivingschool.model;

import java.math.BigDecimal;

public class Enrollment {

  private long enrollmentId;

  private Student student;
  private LicenseCategory licenseCategory;
  private CoursePackage coursePackage;

  private String startDate; // yyyy-MM-dd
  private BigDecimal agreedPrice;
  private int remainingHours;

  public Enrollment(long enrollmentId, Student student, LicenseCategory licenseCategory,
      CoursePackage coursePackage, String startDate, BigDecimal agreedPrice, int remainingHours) {
    this.enrollmentId = enrollmentId;
    this.student = student;
    this.licenseCategory = licenseCategory;
    this.coursePackage = coursePackage;
    this.startDate = startDate;
    this.agreedPrice = agreedPrice;
    this.remainingHours = remainingHours;
  }

  public long getEnrollmentId() {
    return enrollmentId;
  }

  public void setEnrollmentId(long enrollmentId) {
    this.enrollmentId = enrollmentId;
  }

  public Student getStudent() {
    return student;
  }

  public void setStudent(Student student) {
    this.student = student;
  }

  public LicenseCategory getLicenseCategory() {
    return licenseCategory;
  }

  public void setLicenseCategory(LicenseCategory licenseCategory) {
    this.licenseCategory = licenseCategory;
  }

  public CoursePackage getCoursePackage() {
    return coursePackage;
  }

  public void setCoursePackage(CoursePackage coursePackage) {
    this.coursePackage = coursePackage;
  }

  public String getStartDate() {
    return startDate;
  }

  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }

  public BigDecimal getAgreedPrice() {
    return agreedPrice;
  }

  public void setAgreedPrice(BigDecimal agreedPrice) {
    this.agreedPrice = agreedPrice;
  }

  public int getRemainingHours() {
    return remainingHours;
  }

  public void setRemainingHours(int remainingHours) {
    this.remainingHours = remainingHours;
  }

  @Override
  public String toString() {
    return "Enrollment{" +
        "enrollmentId=" + enrollmentId +
        ", student=" + student +
        ", licenseCategory=" + licenseCategory +
        ", coursePackage=" + coursePackage +
        ", startDate='" + startDate + '\'' +
        ", agreedPrice=" + agreedPrice +
        ", remainingHours=" + remainingHours +
        '}';
  }
}
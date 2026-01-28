package com.example.drivingschool.model;

public class Student implements Identifiable {

  private long studentId;
  private String fullName;
  private String phone;
  private String email;
  private String birthDate; // yyyy-MM-dd

  public Student(long studentId, String fullName, String phone, String email, String birthDate) {
    this.studentId = studentId;
    this.fullName = fullName;
    this.phone = phone;
    this.email = email;
    this.birthDate = birthDate;
  }

  public long getStudentId() {
    return studentId;
  }

  public void setStudentId(long studentId) {
    this.studentId = studentId;
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(String birthDate) {
    this.birthDate = birthDate;
  }

  @Override
  public long getId() {
    return studentId;
  }

  @Override
  public String toString() {
    return "Student{" +
        "studentId=" + studentId +
        ", fullName='" + fullName + '\'' +
        ", phone='" + phone + '\'' +
        ", email='" + email + '\'' +
        ", birthDate='" + birthDate + '\'' +
        '}';
  }
}
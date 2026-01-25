package com.example.drivingschool.model;

public class Instructor {

  private long instructorId;
  private String fullName;
  private String phone;

  public Instructor(long instructorId, String fullName, String phone) {
    this.instructorId = instructorId;
    this.fullName = fullName;
    this.phone = phone;
  }

  public long getInstructorId() {
    return instructorId;
  }

  public void setInstructorId(long instructorId) {
    this.instructorId = instructorId;
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

  @Override
  public String toString() {
    return "Instructor{" +
        "instructorId=" + instructorId +
        ", fullName='" + fullName + '\'' +
        ", phone='" + phone + '\'' +
        '}';
  }
}
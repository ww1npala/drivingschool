package com.example.drivingschool.model;

public class DrivingLesson {

  private long lessonId;

  private Enrollment enrollment;
  private Instructor instructor;
  private Car car;

  private String startDateTime; // yyyy-MM-dd HH:mm
  private double durationHours;

  private LessonStatus status;

  public DrivingLesson(long lessonId, Enrollment enrollment, Instructor instructor, Car car,
      String startDateTime, double durationHours, LessonStatus status) {
    this.lessonId = lessonId;
    this.enrollment = enrollment;
    this.instructor = instructor;
    this.car = car;
    this.startDateTime = startDateTime;
    this.durationHours = durationHours;
    this.status = status;
  }

  public long getLessonId() {
    return lessonId;
  }

  public void setLessonId(long lessonId) {
    this.lessonId = lessonId;
  }

  public Enrollment getEnrollment() {
    return enrollment;
  }

  public void setEnrollment(Enrollment enrollment) {
    this.enrollment = enrollment;
  }

  public Instructor getInstructor() {
    return instructor;
  }

  public void setInstructor(Instructor instructor) {
    this.instructor = instructor;
  }

  public Car getCar() {
    return car;
  }

  public void setCar(Car car) {
    this.car = car;
  }

  public String getStartDateTime() {
    return startDateTime;
  }

  public void setStartDateTime(String startDateTime) {
    this.startDateTime = startDateTime;
  }

  public double getDurationHours() {
    return durationHours;
  }

  public void setDurationHours(double durationHours) {
    this.durationHours = durationHours;
  }

  public LessonStatus getStatus() {
    return status;
  }

  public void setStatus(LessonStatus status) {
    this.status = status;
  }

  @Override
  public String toString() {
    return "DrivingLesson{" +
        "lessonId=" + lessonId +
        ", enrollment=" + enrollment +
        ", instructor=" + instructor +
        ", car=" + car +
        ", startDateTime='" + startDateTime + '\'' +
        ", durationHours=" + durationHours +
        ", status=" + status +
        '}';
  }
}
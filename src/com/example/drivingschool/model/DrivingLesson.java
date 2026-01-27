package com.example.drivingschool.model;

public class DrivingLesson {

  private long lessonId;

  private Enrollment enrollment;
  private Instructor instructor;
  private Car car;

  private String startDateTime; // yyyy-MM-ddTHH:mm:ss
  private double durationHours;

  private LessonStatus status;

  // щоб не списати години двічі
  private boolean hoursConsumed;

  public DrivingLesson(long lessonId,
      Enrollment enrollment,
      Instructor instructor,
      Car car,
      String startDateTime,
      double durationHours,
      LessonStatus status) {

    this.lessonId = lessonId;
    this.enrollment = enrollment;
    this.instructor = instructor;
    this.car = car;
    this.startDateTime = startDateTime;
    this.durationHours = durationHours;
    this.status = status;
    this.hoursConsumed = false;
  }


  public boolean isPlanned() {
    return status == LessonStatus.PLANNED;
  }

  public boolean isCompleted() {
    return status == LessonStatus.COMPLETED;
  }

  public boolean isCanceled() {
    return status == LessonStatus.CANCELED;
  }

  /**
   * Завершити урок: - статус стає COMPLETED - години списуються з Enrollment (один раз)
   */
  public void complete() {
    if (status == LessonStatus.CANCELED) {
      throw new IllegalStateException("Неможливо завершити скасований урок");
    }
    if (status == LessonStatus.COMPLETED) {
      return; // вже завершений
    }

    if (enrollment == null) {
      throw new IllegalStateException("Неможливо завершити урок без Enrollment");
    }

    // списуємо години 1 раз
    if (!hoursConsumed) {
      enrollment.consumeHours(durationHours);
      hoursConsumed = true;
    }

    status = LessonStatus.COMPLETED;
  }

  // скасування уроку, повертаємо години назад
  public void cancel() {
    if (status == LessonStatus.CANCELED) {
      return;
    }

    if (status == LessonStatus.COMPLETED && hoursConsumed && enrollment != null) {
      enrollment.refundHours(durationHours);
      hoursConsumed = false;
    }

    status = LessonStatus.CANCELED;
  }

  // геттери сеттери
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

  public boolean isHoursConsumed() {
    return hoursConsumed;
  }

  public void setHoursConsumed(boolean hoursConsumed) {
    this.hoursConsumed = hoursConsumed;
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
        ", hoursConsumed=" + hoursConsumed +
        '}';
  }
}
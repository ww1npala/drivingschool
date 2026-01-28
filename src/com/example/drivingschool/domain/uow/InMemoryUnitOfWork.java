package com.example.drivingschool.domain.uow;

import com.example.drivingschool.model.DrivingLesson;
import com.example.drivingschool.model.Enrollment;
import com.example.drivingschool.model.Payment;
import com.example.drivingschool.repository.Repository;
import com.example.drivingschool.repository.entities.DrivingLessonRepository;
import com.example.drivingschool.repository.entities.EnrollmentRepository;
import com.example.drivingschool.repository.entities.PaymentRepository;

public final class InMemoryUnitOfWork implements UnitOfWork {

  private final EnrollmentRepository enrollmentRepo;
  private final DrivingLessonRepository lessonRepo;
  private final PaymentRepository paymentRepo;

  public InMemoryUnitOfWork(
      EnrollmentRepository enrollmentRepo,
      DrivingLessonRepository lessonRepo,
      PaymentRepository paymentRepo
  ) {
    this.enrollmentRepo = enrollmentRepo;
    this.lessonRepo = lessonRepo;
    this.paymentRepo = paymentRepo;
  }

  @Override
  public Repository<Enrollment> enrollments() {
    return enrollmentRepo;
  }

  @Override
  public Repository<DrivingLesson> lessons() {
    return lessonRepo;
  }

  @Override
  public Repository<Payment> payments() {
    return paymentRepo;
  }

  @Override
  public void commit() {
  }
}
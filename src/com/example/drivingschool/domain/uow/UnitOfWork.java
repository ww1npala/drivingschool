package com.example.drivingschool.domain.uow;

import com.example.drivingschool.model.DrivingLesson;
import com.example.drivingschool.model.Enrollment;
import com.example.drivingschool.model.Payment;
import com.example.drivingschool.repository.Repository;

public interface UnitOfWork {

  Repository<Enrollment> enrollments();

  Repository<DrivingLesson> lessons();

  Repository<Payment> payments();

  void commit();
}
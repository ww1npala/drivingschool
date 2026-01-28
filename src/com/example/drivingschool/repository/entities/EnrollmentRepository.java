package com.example.drivingschool.repository.entities;

import com.example.drivingschool.model.Enrollment;
import com.example.drivingschool.repository.impl.JsonRepository;
import java.nio.file.Path;

public class EnrollmentRepository
    extends JsonRepository<Enrollment> {

  public EnrollmentRepository(Path file) {
    super(file, Enrollment.class);
  }
}
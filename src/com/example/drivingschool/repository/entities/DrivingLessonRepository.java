package com.example.drivingschool.repository.entities;

import com.example.drivingschool.model.DrivingLesson;
import com.example.drivingschool.repository.impl.JsonRepository;
import java.nio.file.Path;

public class DrivingLessonRepository
    extends JsonRepository<DrivingLesson> {

  public DrivingLessonRepository(Path file) {
    super(file, DrivingLesson.class);
  }
}
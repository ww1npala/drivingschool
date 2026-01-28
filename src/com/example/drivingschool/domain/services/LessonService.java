package com.example.drivingschool.domain.services;

import com.example.drivingschool.domain.uow.UnitOfWork;
import com.example.drivingschool.model.DrivingLesson;
import com.example.drivingschool.model.LessonStatus;
import com.example.drivingschool.validation.ModelValidator;
import java.util.ArrayList;
import java.util.List;

public final class LessonService {

  private final UnitOfWork uow;

  public LessonService(UnitOfWork uow) {
    this.uow = uow;
  }

  public DrivingLesson create(DrivingLesson l) {
    ModelValidator.validateLesson(l);
    uow.lessons().create(l);
    uow.commit();
    return l;
  }

  public DrivingLesson getById(long id) {
    return uow.lessons().getById(id);
  }

  public DrivingLesson update(DrivingLesson l) {
    ModelValidator.validateLesson(l);
    uow.lessons().update(l);
    uow.commit();
    return l;
  }

  public boolean delete(long id) {
    boolean ok = uow.lessons().delete(id);
    uow.commit();
    return ok;
  }

  public List<DrivingLesson> getAll() {
    return uow.lessons().getAll();
  }

  // фільтрація уроків за статусом
  public List<DrivingLesson> filterByStatus(LessonStatus status) {
    List<DrivingLesson> all = uow.lessons().getAll();
    List<DrivingLesson> res = new ArrayList<>();
    for (int i = 0; i < all.size(); i++) {
      DrivingLesson l = all.get(i);
      if (l.getStatus() == status) {
        res.add(l);
      }
    }
    return res;
  }

  // пошук уроків для конкретного enrollmentId
  public List<DrivingLesson> findByEnrollmentId(long enrollmentId) {
    List<DrivingLesson> all = uow.lessons().getAll();
    List<DrivingLesson> res = new ArrayList<>();
    for (int i = 0; i < all.size(); i++) {
      DrivingLesson l = all.get(i);
      if (l.getEnrollment() != null && l.getEnrollment().getEnrollmentId() == enrollmentId) {
        res.add(l);
      }
    }
    return res;
  }
}
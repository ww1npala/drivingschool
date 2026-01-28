package com.example.drivingschool.domain.services;

import com.example.drivingschool.domain.uow.UnitOfWork;
import com.example.drivingschool.model.Enrollment;
import com.example.drivingschool.validation.ModelValidator;
import java.util.ArrayList;
import java.util.List;

public final class EnrollmentService {

  private final UnitOfWork uow;

  public EnrollmentService(UnitOfWork uow) {
    this.uow = uow;
  }

  public Enrollment create(Enrollment e) {
    ModelValidator.validateEnrollment(e);
    uow.enrollments().create(e);
    uow.commit();
    return e;
  }

  public Enrollment getById(long id) {
    return uow.enrollments().getById(id);
  }

  public Enrollment update(Enrollment e) {
    ModelValidator.validateEnrollment(e);
    uow.enrollments().update(e);
    uow.commit();
    return e;
  }

  public boolean delete(long id) {
    boolean ok = uow.enrollments().delete(id);
    uow.commit();
    return ok;
  }

  public List<Enrollment> getAll() {
    return uow.enrollments().getAll();
  }

  // пошук, всі записи по категорії | a b c d
  public List<Enrollment> findByCategoryCode(String code) {
    List<Enrollment> all = uow.enrollments().getAll();
    List<Enrollment> res = new ArrayList<>();
    for (int i = 0; i < all.size(); i++) {
      Enrollment e = all.get(i);
      if (e.getLicenseCategory() != null && e.getLicenseCategory().getCode() != null) {
        if (e.getLicenseCategory().getCode().equalsIgnoreCase(code)) {
          res.add(e);
        }
      }
    }
    return res;
  }
}
package com.example.drivingschool.domain.services;

import com.example.drivingschool.domain.uow.UnitOfWork;
import com.example.drivingschool.model.Payment;
import com.example.drivingschool.validation.ModelValidator;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public final class PaymentService {

  private final UnitOfWork uow;

  public PaymentService(UnitOfWork uow) {
    this.uow = uow;
  }

  public Payment create(Payment p) {
    ModelValidator.validatePayment(p);
    uow.payments().create(p);
    uow.commit();
    return p;
  }

  public Payment getById(long id) {
    return uow.payments().getById(id);
  }

  public Payment update(Payment p) {
    ModelValidator.validatePayment(p);
    uow.payments().update(p);
    uow.commit();
    return p;
  }

  public boolean delete(long id) {
    boolean ok = uow.payments().delete(id);
    uow.commit();
    return ok;
  }

  public List<Payment> getAll() {
    return uow.payments().getAll();
  }

  // фільтрація оплати в діапазоні
  public List<Payment> filterByAmountRange(BigDecimal min, BigDecimal max) {
    if (min == null) {
      min = BigDecimal.ZERO;
    }
    if (max == null) {
      max = new BigDecimal("999999999");
    }

    List<Payment> all = uow.payments().getAll();
    List<Payment> res = new ArrayList<>();

    for (int i = 0; i < all.size(); i++) {
      Payment p = all.get(i);
      if (p.getAmount() != null) {
        if (p.getAmount().compareTo(min) >= 0 && p.getAmount().compareTo(max) <= 0) {
          res.add(p);
        }
      }
    }

    return res;
  }

  // пошук всі оплати по enrollmentId
  public List<Payment> findByEnrollmentId(long enrollmentId) {
    List<Payment> all = uow.payments().getAll();
    List<Payment> res = new ArrayList<>();

    for (int i = 0; i < all.size(); i++) {
      Payment p = all.get(i);
      if (p.getEnrollment() != null && p.getEnrollment().getEnrollmentId() == enrollmentId) {
        res.add(p);
      }
    }

    return res;
  }
}
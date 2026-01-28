package com.example.drivingschool.repository.entities;

import com.example.drivingschool.model.Payment;
import com.example.drivingschool.repository.impl.JsonRepository;
import java.nio.file.Path;

public class PaymentRepository
    extends JsonRepository<Payment> {

  public PaymentRepository(Path file) {
    super(file, Payment.class);
  }
}
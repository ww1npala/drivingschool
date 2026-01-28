package com.example.drivingschool.auth;

import java.util.Random;

public final class VerificationCodeService {

  private final Random rnd = new Random();

  public String generate6Digits() {
    int n = 100000 + rnd.nextInt(900000);
    return String.valueOf(n);
  }
}
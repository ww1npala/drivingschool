package com.example.drivingschool.auth;

import org.mindrot.bcrypt.BCrypt;

public final class BCryptPasswordHasher implements PasswordHasher {

  private final int workFactor;

  public BCryptPasswordHasher() {
    this(10);
  }

  public BCryptPasswordHasher(int workFactor) {
    this.workFactor = workFactor;
  }

  @Override
  public String hash(String password) {
    return BCrypt.hashpw(password, BCrypt.gensalt(workFactor));
  }

  @Override
  public boolean verify(String password, String hashed) {
    if (hashed == null || hashed.isBlank()) {
      return false;
    }
    return BCrypt.checkpw(password, hashed);
  }
}
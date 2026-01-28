package com.example.drivingschool.auth;

public class VerificationSession {

  private final String code;
  private final long expiresAtMillis;

  public VerificationSession(String code, long expiresAtMillis) {
    this.code = code;
    this.expiresAtMillis = expiresAtMillis;
  }

  public boolean isExpired() {
    return System.currentTimeMillis() > expiresAtMillis;
  }

  public String getCode() {
    return code;
  }
}
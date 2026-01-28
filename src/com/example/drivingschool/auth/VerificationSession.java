package com.example.drivingschool.auth;

public final class VerificationSession {

  private final String email;
  private final String code;
  private final long expiresAtMillis;

  public VerificationSession(String email, String code, long expiresAtMillis) {
    this.email = email;
    this.code = code;
    this.expiresAtMillis = expiresAtMillis;
  }

  public String getEmail() {
    return email;
  }

  public boolean isExpired() {
    return System.currentTimeMillis() > expiresAtMillis;
  }

  public boolean matches(String inputCode) {
    if (inputCode == null) {
      return false;
    }
    return code.equals(inputCode.trim());
  }
}
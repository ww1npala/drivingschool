package com.example.drivingschool.model;

import java.time.LocalDateTime;

public class User implements Identifiable {

  private final long id;
  private final String login;
  private final String email;
  private final String passwordHash;
  private final UserRole role;
  private boolean emailVerified;
  private final LocalDateTime createdAt;

  public User(long id, String login, String email, String passwordHash, UserRole role) {
    this.id = id;
    this.login = login;
    this.email = email;
    this.passwordHash = passwordHash;
    this.role = role;
    this.emailVerified = false;
    this.createdAt = LocalDateTime.now();
  }

  @Override
  public long getId() {
    return id;
  }

  public String getLogin() {
    return login;
  }

  public String getEmail() {
    return email;
  }

  public String getPasswordHash() {
    return passwordHash;
  }

  public UserRole getRole() {
    return role;
  }

  public boolean isEmailVerified() {
    return emailVerified;
  }

  public void verifyEmail() {
    this.emailVerified = true;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }
}
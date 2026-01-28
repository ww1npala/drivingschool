package com.example.drivingschool.auth;

public interface PasswordHasher {

  String generateSalt();

  String hash(String password, String salt);

  boolean verify(String password, String salt, String expectedHash);
}
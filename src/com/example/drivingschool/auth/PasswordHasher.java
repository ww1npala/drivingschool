package com.example.drivingschool.auth;

public interface PasswordHasher {

  String hash(String password);

  boolean verify(String password, String hashed);
}
package com.example.drivingschool.auth;

public interface EmailSender {

  void send(String to, String subject, String text);
}
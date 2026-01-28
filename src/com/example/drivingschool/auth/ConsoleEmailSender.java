package com.example.drivingschool.auth;

public class ConsoleEmailSender implements EmailSender {

  @Override
  public void send(String to, String subject, String text) {
    System.out.println("\n[email]");
    System.out.println("To: " + to);
    System.out.println(subject);
    System.out.println(text);
    System.out.println();
  }
}
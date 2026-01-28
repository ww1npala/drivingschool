package com.example.drivingschool.auth;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

public final class SmtpEmailSender implements EmailSender {

  private final String username;   // gmail, приклад babaika@gmail.com
  private final String appPassword;

  public SmtpEmailSender(String username, String appPassword) {
    this.username = username;
    this.appPassword = appPassword;
  }

  @Override
  public void send(String to, String subject, String body) {
    try {
      Properties props = new Properties();
      props.put("mail.smtp.auth", "true");
      props.put("mail.smtp.starttls.enable", "true");
      props.put("mail.smtp.host", "smtp.gmail.com");
      props.put("mail.smtp.port", "587");

      Session session = Session.getInstance(props, new Authenticator() {
        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
          return new PasswordAuthentication(username, appPassword);
        }
      });

      Message message = new MimeMessage(session);
      message.setFrom(new InternetAddress(username));
      message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
      message.setSubject(subject);
      message.setText(body);

      Transport.send(message);
    } catch (Exception ex) {
      throw new RuntimeException("SMTP send failed: " + ex.getMessage(), ex);
    }
  }
}
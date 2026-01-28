package com.example.drivingschool.auth;

import com.example.drivingschool.model.User;
import com.example.drivingschool.model.UserRole;
import com.example.drivingschool.repository.entities.UserRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class AuthService {

  private final UserRepository users;
  private final PasswordHasher hasher;
  private final EmailSender emailSender;
  private final VerificationCodeService codes = new VerificationCodeService();

  // email > session (останній код)
  private final Map<String, VerificationSession> sessions = new HashMap<>();

  public AuthService(UserRepository users, PasswordHasher hasher, EmailSender emailSender) {
    this.users = users;
    this.hasher = hasher;
    this.emailSender = emailSender;
  }

  // користувач ввів email - шлемо код, рахує 120 сек
  public void beginEmailVerification(String email) {
    AuthValidator.validateEmail(email);

    String normalized = email.trim().toLowerCase();
    Optional<User> existing = users.findByEmail(normalized);
    if (existing.isPresent()) {
      throw new IllegalArgumentException("Користувач з таким email вже існує");
    }

    String code = codes.generate6Digits();
    long expires = System.currentTimeMillis() + 120_000L;
    VerificationSession session = new VerificationSession(normalized, code, expires);
    sessions.put(normalized, session);

    String subject = "DrivingSchool: код підтвердження";
    String body = "Ваш код підтвердження: " + code + "\nДійсний 2 хвилини.";
    emailSender.send(normalized, subject, body);
  }

  // перевірка коду
  public boolean verifyEmailCode(String email, String code) {
    if (email == null) {
      return false;
    }
    String normalized = email.trim().toLowerCase();

    VerificationSession session = sessions.get(normalized);
    if (session == null) {
      return false;
    }
    if (session.isExpired()) {
      return false;
    }

    return session.matches(code);
  }

  // ресенд, новий код, нові 120 сек
  public void resendCode(String email) {
    beginEmailVerification(email);
  }

  // після вірного коду - реєстрація
  public User registerAfterCode(String login, String email, String password, UserRole role) {
    AuthValidator.validateLogin(login);
    AuthValidator.validateEmail(email);
    AuthValidator.validatePassword(password);

    String normalizedEmail = email.trim().toLowerCase();
    String normalizedLogin = login.trim().toLowerCase();

    // email має бути підтверджений актуальним session
    VerificationSession session = sessions.get(normalizedEmail);
    if (session == null || session.isExpired()) {
      throw new IllegalArgumentException("Код прострочений. Натисни повторну відправку коду.");
    }

    Optional<User> byEmail = users.findByEmail(normalizedEmail);
    if (byEmail.isPresent()) {
      throw new IllegalArgumentException("Користувач з таким email вже існує");
    }

    Optional<User> byLogin = users.findByLogin(normalizedLogin);
    if (byLogin.isPresent()) {
      throw new IllegalArgumentException("Такий login вже зайнятий");
    }

    long id = users.nextId();
    String hash = hasher.hash(password);

    User u = new User(id, login.trim(), normalizedEmail, hash, role);
    u.verifyEmail();

    users.create(u);
    sessions.remove(normalizedEmail);
    return u;
  }

  // логін по login + password
  public User login(String login, String password) {
    AuthValidator.validateLogin(login);
    AuthValidator.validatePassword(password);

    User u = users.findByLogin(login.trim().toLowerCase())
        .orElseThrow(() -> new IllegalArgumentException("Невірний login або пароль"));

    if (!u.isEmailVerified()) {
      throw new IllegalArgumentException("Email не підтверджено");
    }

    if (!hasher.verify(password, u.getPasswordHash())) {
      throw new IllegalArgumentException("Невірний login або пароль");
    }

    return u;
  }
}
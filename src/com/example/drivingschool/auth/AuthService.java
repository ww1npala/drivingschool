package com.example.drivingschool.auth;

import com.example.drivingschool.model.User;
import com.example.drivingschool.model.UserRole;
import com.example.drivingschool.repository.entities.UserRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class AuthService {

  private final UserRepository userRepo;
  private final EmailSender emailSender;
  private final Map<String, VerificationSession> pending = new HashMap<>();

  public AuthService(UserRepository userRepo, EmailSender emailSender) {
    this.userRepo = userRepo;
    this.emailSender = emailSender;
  }

  public void startRegistration(String email, String password) {
    if (userRepo.findByEmail(email) != null) {
      throw new IllegalArgumentException("Email вже зареєстрований");
    }
    sendNewCode(email);
  }

  public void confirmCode(String email, String code, String password) {
    VerificationSession session = pending.get(email);

    if (session == null || session.isExpired() || !session.getCode().equals(code)) {
      sendNewCode(email);
      throw new IllegalArgumentException("Невірний код. Новий код надіслано.");
    }

    long id = System.currentTimeMillis();
    User user = new User(id, email, hash(password), UserRole.USER);
    user.verifyEmail();

    userRepo.create(user);
    pending.remove(email);
  }

  public User login(String email, String password) {
    User user = userRepo.findByEmail(email);
    if (user == null || !user.isEmailVerified()) {
      throw new IllegalArgumentException("Невірні дані");
    }
    if (!user.getPasswordHash().equals(hash(password))) {
      throw new IllegalArgumentException("Невірні дані");
    }
    return user;
  }

  private void sendNewCode(String email) {
    String code = String.valueOf(100000 + new Random().nextInt(900000));
    long expiresAt = System.currentTimeMillis() + 120_000;

    pending.put(email, new VerificationSession(code, expiresAt));

    emailSender.send(
        email,
        "Код підтвердження",
        "Код: " + code + " (діє 120 секунд)"
    );
  }

  private String hash(String raw) {
    return Integer.toHexString(raw.hashCode());
  }
}
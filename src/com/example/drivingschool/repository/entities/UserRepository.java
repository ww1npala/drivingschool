package com.example.drivingschool.repository.entities;

import com.example.drivingschool.model.User;
import com.example.drivingschool.repository.impl.JsonRepository;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public class UserRepository extends JsonRepository<User> {

  public UserRepository(Path file) {
    super(file, User.class);
  }

  public Optional<User> findByEmail(String email) {
    if (email == null) {
      return Optional.empty();
    }
    String e = email.trim().toLowerCase();

    List<User> all = getAll();
    for (int i = 0; i < all.size(); i++) {
      User u = all.get(i);
      if (u.getEmail() != null && u.getEmail().trim().toLowerCase().equals(e)) {
        return Optional.of(u);
      }
    }
    return Optional.empty();
  }

  public Optional<User> findByLogin(String login) {
    if (login == null) {
      return Optional.empty();
    }
    String l = login.trim().toLowerCase();

    List<User> all = getAll();
    for (int i = 0; i < all.size(); i++) {
      User u = all.get(i);
      if (u.getLogin() != null && u.getLogin().trim().toLowerCase().equals(l)) {
        return Optional.of(u);
      }
    }
    return Optional.empty();
  }

  public long nextId() {
    long max = 0;
    List<User> all = getAll();
    for (int i = 0; i < all.size(); i++) {
      long id = all.get(i).getId();
      if (id > max) {
        max = id;
      }
    }
    return max + 1;
  }
}
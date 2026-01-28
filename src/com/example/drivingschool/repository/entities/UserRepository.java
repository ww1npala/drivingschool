package com.example.drivingschool.repository.entities;

import com.example.drivingschool.model.User;
import com.example.drivingschool.repository.impl.JsonRepository;
import java.nio.file.Path;

public class UserRepository extends JsonRepository<User> {

  public UserRepository(Path file) {
    super(file, User.class);
  }

  public User findByEmail(String email) {
    for (User u : getAll()) {
      if (u.getEmail().equalsIgnoreCase(email)) {
        return u;
      }
    }
    return null;
  }
}
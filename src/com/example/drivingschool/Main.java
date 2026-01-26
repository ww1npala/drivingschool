package com.example.drivingschool;

import com.example.drivingschool.generator.DemoDataGenerator;
import com.example.drivingschool.model.Enrollment;
import com.example.drivingschool.storage.JsonFileStorage;
import com.example.drivingschool.validation.ModelValidator;
import java.nio.file.Path;
import java.util.List;

public class Main {

  public static void main(String[] args) {

    // генеруємо
    List<Enrollment> enrollments = DemoDataGenerator.generateEnrollments(20);

    // валідація
    for (int i = 0; i < enrollments.size(); i++) {
      ModelValidator.validateEnrollment(enrollments.get(i));
    }

    // зберігаємо
    Path file = Path.of("out", "enrollments.json");
    JsonFileStorage.saveList(file, enrollments);

    // читаємо назад
    List<Enrollment> loaded = JsonFileStorage.loadList(file, Enrollment.class);

    // результат
    System.out.println("Saved: " + enrollments.size());
    System.out.println("Loaded: " + loaded.size());
    if (!loaded.isEmpty()) {
      System.out.println("First loaded item:");
      System.out.println(loaded.get(0));
    }
  }
}
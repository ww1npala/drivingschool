package com.example.drivingschool.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

public final class JsonFileStorage {

  private static final Gson GSON = new GsonBuilder()
      .setPrettyPrinting()
      .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
      .create();

  private JsonFileStorage() {
  }

  public static <T> void saveList(Path file, List<T> list) {
    try {
      Path parent = file.getParent();
      if (parent != null) {
        Files.createDirectories(parent);
      }

      String json = GSON.toJson(list);
      Files.writeString(file, json, StandardCharsets.UTF_8);

    } catch (IOException e) {
      throw new RuntimeException("Failed to save JSON to file: " + file, e);
    }
  }

  public static <T> List<T> loadList(Path file, Class<T> elementClass) {
    try {
      if (!Files.exists(file)) {
        return List.of(); // важливо: якщо users.json ще не існує
      }

      String json = Files.readString(file, StandardCharsets.UTF_8);
      if (json.isBlank()) {
        return List.of();
      }

      Type type = TypeToken.getParameterized(List.class, elementClass).getType();
      return GSON.fromJson(json, type);

    } catch (IOException e) {
      throw new RuntimeException("Failed to load JSON from file: " + file, e);
    }
  }
}
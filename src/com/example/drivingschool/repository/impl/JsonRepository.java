package com.example.drivingschool.repository.impl;

import com.example.drivingschool.model.Identifiable;
import com.example.drivingschool.storage.JsonFileStorage;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class JsonRepository<T extends Identifiable> extends InMemoryRepository<T> {

  private final Path file;
  private final Class<T> clazz;

  public JsonRepository(Path file, Class<T> clazz) {
    this.file = file;
    this.clazz = clazz;
    loadFromFile();
  }

  private void loadFromFile() {
    if (file == null || clazz == null) {
      return;
    }
    if (!Files.exists(file)) {
      return;
    }

    List<T> loaded = JsonFileStorage.loadList(file, clazz);
    for (int i = 0; i < loaded.size(); i++) {
      T entity = loaded.get(i);
      if (entity != null) {
        identityMap.put(entity.getId(), entity);
      }
    }
  }

  private void saveToFile() {
    JsonFileStorage.saveList(file, getAll());
  }

  @Override
  public void create(T entity) {
    super.create(entity);
    saveToFile();
  }

  @Override
  public void update(T entity) {
    super.update(entity);
    saveToFile();
  }

  @Override
  public boolean delete(long id) {
    boolean ok = super.delete(id);
    if (ok) {
      saveToFile();
    }
    return ok;
  }
}
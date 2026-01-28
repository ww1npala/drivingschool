package com.example.drivingschool.repository.impl;

import com.example.drivingschool.model.Identifiable;
import com.example.drivingschool.repository.IdentityMap;
import com.example.drivingschool.repository.Repository;
import java.util.ArrayList;
import java.util.List;

public class InMemoryRepository<T extends Identifiable> implements Repository<T> {

  protected final IdentityMap<T> identityMap = new IdentityMap<>();

  @Override
  public void create(T entity) {
    long id = entity.getId();
    if (identityMap.contains(id)) {
      throw new IllegalArgumentException("Сутність з таким id вже існує: " + id);
    }
    identityMap.put(id, entity);
  }

  @Override
  public T getById(long id) {
    T entity = identityMap.get(id);
    if (entity == null) {
      throw new IllegalArgumentException("Сутність з id " + id + " не знайдена");
    }
    return entity;
  }

  @Override
  public List<T> getAll() {
    return new ArrayList<>(identityMap.values());
  }

  @Override
  public void update(T entity) {
    long id = entity.getId();
    if (!identityMap.contains(id)) {
      throw new IllegalArgumentException("Немає сутності з id " + id);
    }
    identityMap.put(id, entity);
  }

  @Override
  public boolean delete(long id) {
    if (!identityMap.contains(id)) {
      return false;
    }
    identityMap.remove(id);
    return true;
  }
}
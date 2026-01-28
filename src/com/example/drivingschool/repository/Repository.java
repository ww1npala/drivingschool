package com.example.drivingschool.repository;

import com.example.drivingschool.model.Identifiable;
import java.util.List;
import java.util.Optional;

public interface Repository<T extends Identifiable> {

  void create(T entity);

  T getById(long id);

  List<T> getAll();

  void update(T entity);

  boolean delete(long id);

  default Optional<T> findById(long id) {
    try {
      return Optional.of(getById(id));
    } catch (RuntimeException ex) {
      return Optional.empty();
    }
  }

  default List<T> findAll() {
    return getAll();
  }

  default boolean remove(long id) {
    return delete(id);
  }
}
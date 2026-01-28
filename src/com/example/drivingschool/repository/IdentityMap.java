package com.example.drivingschool.repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class IdentityMap<T> {

  private final Map<Long, T> map = new HashMap<>();

  public boolean contains(long id) {
    return map.containsKey(id);
  }

  public T get(long id) {
    return map.get(id);
  }

  public void put(long id, T entity) {
    map.put(id, entity);
  }

  public void remove(long id) {
    map.remove(id);
  }

  public Collection<T> values() {
    return map.values();
  }
}
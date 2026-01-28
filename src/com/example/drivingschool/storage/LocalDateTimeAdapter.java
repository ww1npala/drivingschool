package com.example.drivingschool.storage;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public final class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {

  @Override
  public void write(JsonWriter out, LocalDateTime value) throws IOException {
    if (value == null) {
      out.nullValue();
      return;
    }
    out.value(value.toString()); // ISO-8601
  }

  @Override
  public LocalDateTime read(JsonReader in) throws IOException {
    String s = in.nextString();
    if (s == null || s.isBlank()) {
      return null;
    }
    return LocalDateTime.parse(s);
  }
}
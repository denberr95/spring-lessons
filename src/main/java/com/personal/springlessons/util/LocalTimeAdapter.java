package com.personal.springlessons.util;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

import org.jspecify.annotations.Nullable;

public class LocalTimeAdapter extends XmlAdapter<String, LocalTime> {

  @Override
  public @Nullable LocalTime unmarshal(@Nullable String v) {
    return v != null ? LocalTime.parse(v) : null;
  }

  @Override
  public @Nullable String marshal(@Nullable LocalTime v) {
    return v != null ? v.format(DateTimeFormatter.ISO_TIME) : null;
  }
}

package com.personal.springlessons.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

import org.jspecify.annotations.Nullable;

public class LocalDateAdapter extends XmlAdapter<String, LocalDate> {

  @Override
  public @Nullable LocalDate unmarshal(@Nullable String v) {
    return v != null ? LocalDate.parse(v) : null;
  }

  @Override
  public @Nullable String marshal(@Nullable LocalDate v) {
    return v != null ? v.format(DateTimeFormatter.ISO_DATE) : null;
  }
}

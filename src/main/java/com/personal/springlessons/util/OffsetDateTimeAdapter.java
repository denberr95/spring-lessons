package com.personal.springlessons.util;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

import org.jspecify.annotations.Nullable;

public class OffsetDateTimeAdapter extends XmlAdapter<String, OffsetDateTime> {

  @Override
  public @Nullable OffsetDateTime unmarshal(@Nullable String v) {
    return v != null ? OffsetDateTime.parse(v) : null;
  }

  @Override
  public @Nullable String marshal(@Nullable OffsetDateTime v) {
    return v != null ? v.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME) : null;
  }
}

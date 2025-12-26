package com.personal.springlessons.util;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

public class OffsetDateTimeAdapter extends XmlAdapter<String, OffsetDateTime> {

  @Override
  public OffsetDateTime unmarshal(String v) {
    return v != null ? OffsetDateTime.parse(v) : null;
  }

  @Override
  public String marshal(OffsetDateTime v) {
    return v != null ? v.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME) : null;
  }
}

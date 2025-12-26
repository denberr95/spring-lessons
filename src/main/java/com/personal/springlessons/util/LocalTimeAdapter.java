package com.personal.springlessons.util;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

public class LocalTimeAdapter extends XmlAdapter<String, LocalTime> {

  @Override
  public LocalTime unmarshal(String v) {
    return v != null ? LocalTime.parse(v) : null;
  }

  @Override
  public String marshal(LocalTime v) {
    return v != null ? v.format(DateTimeFormatter.ISO_TIME) : null;
  }
}

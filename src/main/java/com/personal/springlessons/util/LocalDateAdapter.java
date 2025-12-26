package com.personal.springlessons.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

public class LocalDateAdapter extends XmlAdapter<String, LocalDate> {

  @Override
  public LocalDate unmarshal(String v) {
    return v != null ? LocalDate.parse(v) : null;
  }

  @Override
  public String marshal(LocalDate v) {
    return v != null ? v.format(DateTimeFormatter.ISO_DATE) : null;
  }
}

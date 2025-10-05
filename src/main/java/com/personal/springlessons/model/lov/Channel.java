package com.personal.springlessons.model.lov;

import java.util.Arrays;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(enumAsRef = true)
public enum Channel {
  POSTMAN("Postman"), INSOMNIA("Insomnia"), SOAPUI("SoapUI"), NA("Not Available");

  private final String value;

  @Override
  public String toString() {
    return this.value;
  }

  @JsonValue
  public String getValue() {
    return this.value;
  }

  @JsonCreator
  public static Channel fromString(String value) {
    return Arrays.stream(Channel.values()).filter(channel -> channel.value.equalsIgnoreCase(value))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException(String.format(
            "Invalid value '%s'. Allowed values are: %s", value, Arrays.stream(Channel.values())
                .map(Channel::getValue).collect(Collectors.joining(", ")))));
  }

  private Channel(String value) {
    this.value = value;
  }
}

package com.personal.springlessons.model.lov;

import java.util.Arrays;
import java.util.stream.Collectors;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;

@Schema(enumAsRef = true)
@AllArgsConstructor
public enum Genre {
    NOIR("Noir"), FANTASY("Fantasy"), SCIENCE_FICTION("Science Fiction"), MYSTERY(
            "Mystery"), ROMANCE("Romance"), NA("Not Available");

    private final String value;

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }

    @JsonValue
    public String getValue() {
        return this.value;
    }

    @JsonCreator
    public static Genre fromString(String value) {
        return Arrays.stream(Genre.values()).filter(domain -> domain.value.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("Invalid value '%s'. Allowed values are: %s", value,
                                Arrays.stream(Genre.values()).map(Genre::getValue)
                                        .collect(Collectors.joining(", ")))));
    }
}

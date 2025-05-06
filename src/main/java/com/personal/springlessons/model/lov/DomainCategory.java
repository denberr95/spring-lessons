package com.personal.springlessons.model.lov;

import java.util.Arrays;
import java.util.stream.Collectors;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(enumAsRef = true, name = "Category")
public enum DomainCategory {
    BOOKS("Books"), ITEMS("Items"), NA("Not Available");

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
    public static DomainCategory fromString(String value) {
        return Arrays.stream(DomainCategory.values())
                .filter(domain -> domain.value.equalsIgnoreCase(value)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("Invalid value '%s'. Allowed values are: %s", value,
                                Arrays.stream(DomainCategory.values()).map(DomainCategory::getValue)
                                        .collect(Collectors.joining(", ")))));
    }

    private DomainCategory(String value) {
        this.value = value;
    }
}

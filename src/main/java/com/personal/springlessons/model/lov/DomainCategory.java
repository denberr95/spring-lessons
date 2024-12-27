package com.personal.springlessons.model.lov;

import java.util.Arrays;
import java.util.stream.Collectors;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;

@Schema(enumAsRef = true)
@AllArgsConstructor
public enum DomainCategory {
    BOOKS("Books"), ITEMS("Items"), ND("Not Defined");

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
    public static DomainCategory fromValue(String value) {
        for (DomainCategory e : DomainCategory.values()) {
            if (e.value.equals(value)) {
                return e;
            }
        }
        String allowedValues = Arrays.stream(DomainCategory.values()).map(DomainCategory::getValue)
                .collect(Collectors.joining(", "));
        throw new IllegalArgumentException(String
                .format("Unexpected value '%s', allowed values are: '%s'", value, allowedValues));
    }
}

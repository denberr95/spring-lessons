package com.personal.springlessons.model.lov;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum APICategory {
    BOOKS("Books"), ND("Not Defined");

    private String value;

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }

    @JsonValue
    public String getValue() {
        return this.value;
    }

    @JsonCreator
    public static APICategory getValue(String value) {
        for (APICategory e : APICategory.values()) {
            if (e.value.equals(value)) {
                return e;
            }
        }
        throw new IllegalArgumentException(String.format("Unexpected value '%s'", value));
    }
}

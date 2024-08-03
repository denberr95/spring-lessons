package com.personal.springlessons.model.lov;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum DomainCategory {
    BOOKS("Books"), ITEMS("Items"), ND("Not Defined");

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
    public static DomainCategory getValue(String value) {
        for (DomainCategory e : DomainCategory.values()) {
            if (e.value.equals(value)) {
                return e;
            }
        }
        throw new IllegalArgumentException(String.format("Unexpected value '%s'", value));
    }
}

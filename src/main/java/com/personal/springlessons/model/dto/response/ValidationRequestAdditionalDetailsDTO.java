package com.personal.springlessons.model.dto.response;

import java.util.Objects;

public class ValidationRequestAdditionalDetailsDTO {

    private String field;
    private String message;
    private String value;

    public ValidationRequestAdditionalDetailsDTO(String field, String message, String value) {
        this.field = field;
        this.message = message;
        this.value = value;
    }

    public ValidationRequestAdditionalDetailsDTO() {}

    public String getField() {
        return this.field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.field, this.message, this.value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;
        ValidationRequestAdditionalDetailsDTO other = (ValidationRequestAdditionalDetailsDTO) obj;
        return Objects.equals(this.field, other.field)
                && Objects.equals(this.message, other.message)
                && Objects.equals(this.value, other.value);
    }

    @Override
    public String toString() {
        return "ValidationRequestAdditionalDetailsDTO [field=" + this.field + ", message="
                + this.message + ", value=" + this.value + "]";
    }

}

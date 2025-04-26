package com.personal.springlessons.model.dto;

import java.util.Objects;

public class CsvRowValidationDTO {

    private String field;
    private Object value;
    private String message;

    public CsvRowValidationDTO(String field, Object value, String message) {
        this.field = field;
        this.value = value;
        this.message = message;
    }

    public CsvRowValidationDTO() {}

    public String getField() {
        return this.field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Object getValue() {
        return this.value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.field, this.value, this.message);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;
        CsvRowValidationDTO other = (CsvRowValidationDTO) obj;
        return Objects.equals(this.field, other.field) && Objects.equals(this.value, other.value)
                && Objects.equals(this.message, other.message);
    }

    @Override
    public String toString() {
        return "CsvRowValidationDTO [field=" + this.field + ", value=" + this.value + ", message="
                + this.message + "]";
    }
}

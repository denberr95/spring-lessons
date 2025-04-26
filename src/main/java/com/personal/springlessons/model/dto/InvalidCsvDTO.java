package com.personal.springlessons.model.dto;

import java.util.List;
import java.util.Objects;

public class InvalidCsvDTO {

    private Integer row;
    private List<CsvRowValidationDTO> validations;

    public InvalidCsvDTO(Integer row, List<CsvRowValidationDTO> validations) {
        this.row = row;
        this.validations = validations;
    }

    public InvalidCsvDTO() {}

    public Integer getRow() {
        return this.row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public List<CsvRowValidationDTO> getValidations() {
        return this.validations;
    }

    public void setValidations(List<CsvRowValidationDTO> validations) {
        this.validations = validations;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.row, this.validations);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;
        InvalidCsvDTO other = (InvalidCsvDTO) obj;
        return Objects.equals(this.row, other.row)
                && Objects.equals(this.validations, other.validations);
    }

    @Override
    public String toString() {
        return "InvalidCsvDTO [row=" + this.row + ", validations=" + this.validations + "]";
    }

}

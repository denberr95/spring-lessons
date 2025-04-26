package com.personal.springlessons.model.dto.response;

import java.util.Objects;

public class InvalidUUIDAdditionalDetailsDTO {

    private String invalidId;

    public InvalidUUIDAdditionalDetailsDTO(String invalidId) {
        this.invalidId = invalidId;
    }

    public InvalidUUIDAdditionalDetailsDTO() {}

    public String getInvalidId() {
        return this.invalidId;
    }

    public void setInvalidId(String invalidId) {
        this.invalidId = invalidId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.invalidId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;
        InvalidUUIDAdditionalDetailsDTO other = (InvalidUUIDAdditionalDetailsDTO) obj;
        return Objects.equals(this.invalidId, other.invalidId);
    }

    @Override
    public String toString() {
        return "InvalidUUIAdditionalDetailsDTO [invalidId=" + this.invalidId + "]";
    }

}

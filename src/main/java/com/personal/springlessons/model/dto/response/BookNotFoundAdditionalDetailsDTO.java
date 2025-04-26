package com.personal.springlessons.model.dto.response;

import java.util.Objects;

public class BookNotFoundAdditionalDetailsDTO {
    
    private String id;

    public BookNotFoundAdditionalDetailsDTO(String id) {
        this.id = id;
    }

    public BookNotFoundAdditionalDetailsDTO() {}

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;
        BookNotFoundAdditionalDetailsDTO other = (BookNotFoundAdditionalDetailsDTO) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "BookNotFoundAdditionalDetailsDTO [id=" + this.id + "]";
    }
}

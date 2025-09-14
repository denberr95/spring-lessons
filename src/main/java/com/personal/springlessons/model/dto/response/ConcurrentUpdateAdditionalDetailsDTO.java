package com.personal.springlessons.model.dto.response;

import java.util.Objects;

public class ConcurrentUpdateAdditionalDetailsDTO {

    private String id;
    private Long version;

    public ConcurrentUpdateAdditionalDetailsDTO(String id, Long version) {
        this.id = id;
        this.version = version;
    }

    public ConcurrentUpdateAdditionalDetailsDTO() {}

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getVersion() {
        return this.version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.version);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;
        ConcurrentUpdateAdditionalDetailsDTO other = (ConcurrentUpdateAdditionalDetailsDTO) obj;
        return Objects.equals(this.id, other.id) && Objects.equals(this.version, other.version);
    }

    @Override
    public String toString() {
        return "ConcurrentUpdateAdditionalDetailsDTO [version=" + this.version + "]";
    }
}

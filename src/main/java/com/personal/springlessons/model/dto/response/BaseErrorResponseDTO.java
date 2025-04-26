package com.personal.springlessons.model.dto.response;

import java.time.OffsetDateTime;
import java.util.Objects;
import com.personal.springlessons.model.lov.DomainCategory;

public class BaseErrorResponseDTO {

    private OffsetDateTime timestamp = OffsetDateTime.now();
    private DomainCategory category;
    private String message;

    public BaseErrorResponseDTO(OffsetDateTime timestamp, DomainCategory category, String message) {
        this.timestamp = timestamp;
        this.category = category;
        this.message = message;
    }

    public BaseErrorResponseDTO() {}

    public OffsetDateTime getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(OffsetDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public DomainCategory getCategory() {
        return this.category;
    }

    public void setCategory(DomainCategory category) {
        this.category = category;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.timestamp, this.category, this.message);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;
        BaseErrorResponseDTO other = (BaseErrorResponseDTO) obj;
        return Objects.equals(this.timestamp, other.timestamp) && this.category == other.category
                && Objects.equals(this.message, other.message);
    }

    @Override
    public String toString() {
        return "BaseErrorResponseDTO [timestamp=" + this.timestamp + ", category=" + this.category
                + ", message=" + this.message + "]";
    }
}

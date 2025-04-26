package com.personal.springlessons.model.dto.response;

import java.util.Objects;

public class GenericErrorAdditionalDetailsDTO {
    
    private String exceptionName;
    private String exceptionMessage;

    public GenericErrorAdditionalDetailsDTO(String exceptionName, String exceptionMessage) {
        this.exceptionName = exceptionName;
        this.exceptionMessage = exceptionMessage;
    }

    public GenericErrorAdditionalDetailsDTO() {}

    public String getExceptionName() {
        return this.exceptionName;
    }

    public void setExceptionName(String exceptionName) {
        this.exceptionName = exceptionName;
    }

    public String getExceptionMessage() {
        return this.exceptionMessage;
    }

    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.exceptionName, this.exceptionMessage);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;
        GenericErrorAdditionalDetailsDTO other = (GenericErrorAdditionalDetailsDTO) obj;
        return Objects.equals(this.exceptionName, other.exceptionName)
                && Objects.equals(this.exceptionMessage, other.exceptionMessage);
    }

    @Override
    public String toString() {
        return "GenericErrorAdditionalDetailsDTO [exceptionName=" + this.exceptionName
                + ", exceptionMessage=" + this.exceptionMessage + "]";
    }
}

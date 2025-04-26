package com.personal.springlessons.model.dto.response;

import java.util.Objects;

public class MissingHttpRequestHeaderAdditionalDetailsDTO {

    private String header;

    public MissingHttpRequestHeaderAdditionalDetailsDTO(String header) {
        this.header = header;
    }

    public MissingHttpRequestHeaderAdditionalDetailsDTO() {}

    public String getHeader() {
        return this.header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.header);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;
        MissingHttpRequestHeaderAdditionalDetailsDTO other =
                (MissingHttpRequestHeaderAdditionalDetailsDTO) obj;
        return Objects.equals(this.header, other.header);
    }

    @Override
    public String toString() {
        return "MissingHttpRequestHeaderAdditionalDetailsDTO [header=" + this.header + "]";
    }

}

package com.personal.springlessons.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FileInformationRequest {

    @JsonProperty
    private byte[] fileContent;

    @JsonProperty
    private String fileName;
}

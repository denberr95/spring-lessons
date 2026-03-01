package com.personal.springlessons.model.dto.response;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InvalidFileTypeAdditionalDetailsDTO {

  private String fileName;
  private List<String> validFileTypes;
}

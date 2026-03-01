package com.personal.springlessons.model.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ValidationRequestAdditionalDetailsDTO {

  private String field;
  private String message;
  private String value;
}

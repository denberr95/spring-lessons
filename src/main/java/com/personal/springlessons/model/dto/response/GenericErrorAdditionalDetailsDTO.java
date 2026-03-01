package com.personal.springlessons.model.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GenericErrorAdditionalDetailsDTO {

  private String exceptionName;
  private String exceptionMessage;
}

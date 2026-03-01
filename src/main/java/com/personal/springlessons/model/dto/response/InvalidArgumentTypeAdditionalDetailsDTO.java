package com.personal.springlessons.model.dto.response;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InvalidArgumentTypeAdditionalDetailsDTO {

  private String field;
  private String value;
  private List<String> pickList;
}

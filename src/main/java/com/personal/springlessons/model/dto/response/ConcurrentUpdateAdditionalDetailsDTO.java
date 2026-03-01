package com.personal.springlessons.model.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ConcurrentUpdateAdditionalDetailsDTO {

  private String id;
  private String version;
}

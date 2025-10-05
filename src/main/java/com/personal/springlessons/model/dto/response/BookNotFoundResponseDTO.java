package com.personal.springlessons.model.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class BookNotFoundResponseDTO extends BaseErrorResponseDTO {

  private BookNotFoundAdditionalDetailsDTO additionalData;
}

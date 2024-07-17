package com.personal.springlessons.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BookNotFoundResponseDTO extends BaseErrorResponseDTO {

    private String id;
}

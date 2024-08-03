package com.personal.springlessons.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class InvalidUUIDResponseDTO extends BaseErrorResponseDTO {

    private Details additionalData;

    @Data
    @NoArgsConstructor
    public class Details {
        private String invalidId;
    }
}

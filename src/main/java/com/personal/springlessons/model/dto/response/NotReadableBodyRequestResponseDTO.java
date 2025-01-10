package com.personal.springlessons.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class NotReadableBodyRequestResponseDTO extends BaseErrorResponseDTO {

    private Details additionalData;

    @Data
    @NoArgsConstructor
    @Schema(name = "NotReadableBodyRequestAdditionalDetailsDTO")
    public class Details {
        private String exception;
    }
}

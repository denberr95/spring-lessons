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
public class MissingHttpRequestHeaderResponseDTO extends BaseErrorResponseDTO {

    private Details additionalData;

    @Data
    @NoArgsConstructor
    @Schema(name = "MissingHttpRequestHeaderAdditionalDetailsDTO")
    public class Details {
        private String header;
    }
}

package com.personal.springlessons.model.dto.response;

import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class InvalidArgumentTypeResponseDTO extends BaseErrorResponseDTO {

    private Details additionalData;

    @Data
    @NoArgsConstructor
    @Schema(name = "InvalidArgumentTypeAdditionalDetailsDTO")
    public class Details {
        private String field;
        private String value;
        private List<String> pickList;
    }
}

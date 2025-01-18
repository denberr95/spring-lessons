package com.personal.springlessons.model.dto.response;

import java.util.List;
import com.personal.springlessons.model.dto.InvalidCsvDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class InvalidCSVContentResponseDTO extends BaseErrorResponseDTO {
    
    private Integer totalRows;
    private Details additionalData;
    
    @Data
    @NoArgsConstructor
    @Schema(name = "InvalidCSVContentAdditionalDetailsDTO")
    public class Details {
        private List<InvalidCsvDTO> rows;
    }
}

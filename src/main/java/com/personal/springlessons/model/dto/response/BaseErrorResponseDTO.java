package com.personal.springlessons.model.dto.response;

import java.time.OffsetDateTime;
import com.personal.springlessons.model.lov.DomainCategory;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BaseErrorResponseDTO {

    private OffsetDateTime timestamp = OffsetDateTime.now();

    private DomainCategory category;
    
    private String message;
}

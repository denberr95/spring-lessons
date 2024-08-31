package com.personal.springlessons.model.dto;

import java.time.LocalDateTime;
import com.personal.springlessons.model.lov.DomainCategory;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BaseErrorResponseDTO {

    private LocalDateTime timestamp = LocalDateTime.now();

    private DomainCategory category;
    
    private String message;
}

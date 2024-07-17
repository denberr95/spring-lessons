package com.personal.springlessons.model.dto;

import java.time.LocalDateTime;
import com.personal.springlessons.model.lov.APICategory;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BaseErrorResponseDTO {

    private LocalDateTime timestamp;

    private APICategory category;
    
    private String message;
}

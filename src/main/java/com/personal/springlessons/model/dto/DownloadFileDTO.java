package com.personal.springlessons.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DownloadFileDTO {
    
    private String fileName;

    private byte[] content;
}

package com.personal.springlessons.model.dto.response;

import java.util.List;

import com.personal.springlessons.model.dto.InvalidCsvDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InvalidCSVContentAdditionalDetailsDTO {

  private List<InvalidCsvDTO> rows;
}

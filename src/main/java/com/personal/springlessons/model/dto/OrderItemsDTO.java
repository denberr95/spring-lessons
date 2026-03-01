package com.personal.springlessons.model.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import com.personal.springlessons.util.Constants;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderItemsDTO {

  private String id;

  private Integer quantity;

  @Size(min = Constants.I_VAL_1, max = Constants.I_VAL_100,
      message = Constants.ERROR_MSG_LIST_SIZE_VALIDATION)
  @NotEmpty(message = Constants.ERROR_MSG_NOT_BLANK) @Valid private List<ItemDTO> items;
}

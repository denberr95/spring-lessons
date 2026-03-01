package com.personal.springlessons.model.dto.wrapper;

import java.util.List;

import com.personal.springlessons.model.dto.OrderItemsDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderItemsWrapperDTO {

  private List<OrderItemsDTO> content;
  private int page;
  private int size;
  private long totalElements;
  private int totalPages;
}

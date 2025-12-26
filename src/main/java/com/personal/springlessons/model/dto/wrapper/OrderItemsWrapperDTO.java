package com.personal.springlessons.model.dto.wrapper;

import java.util.List;
import java.util.Objects;

import com.personal.springlessons.model.dto.OrderItemsDTO;

public class OrderItemsWrapperDTO {

  private List<OrderItemsDTO> content;
  private int page;
  private int size;
  private long totalElements;
  private int totalPages;

  public OrderItemsWrapperDTO(List<OrderItemsDTO> content, int page, int size, long totalElements,
      int totalPages) {
    this.content = content;
    this.page = page;
    this.size = size;
    this.totalElements = totalElements;
    this.totalPages = totalPages;
  }

  public OrderItemsWrapperDTO() {}

  public List<OrderItemsDTO> getContent() {
    return this.content;
  }

  public void setContent(List<OrderItemsDTO> content) {
    this.content = content;
  }

  public int getPage() {
    return this.page;
  }

  public void setPage(int page) {
    this.page = page;
  }

  public int getSize() {
    return this.size;
  }

  public void setSize(int size) {
    this.size = size;
  }

  public long getTotalElements() {
    return this.totalElements;
  }

  public void setTotalElements(long totalElements) {
    this.totalElements = totalElements;
  }

  public int getTotalPages() {
    return this.totalPages;
  }

  public void setTotalPages(int totalPages) {
    this.totalPages = totalPages;
  }

  @Override
  public String toString() {
    return "OrderItemsWrapperDTO [content=" + this.content + ", page=" + this.page + ", size="
        + this.size + ", totalElements=" + this.totalElements + ", totalPages=" + this.totalPages
        + "]";
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.content, this.page, this.size, this.totalElements, this.totalPages);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (this.getClass() != obj.getClass())
      return false;
    OrderItemsWrapperDTO other = (OrderItemsWrapperDTO) obj;
    return Objects.equals(this.content, other.content) && this.page == other.page
        && this.size == other.size && this.totalElements == other.totalElements
        && this.totalPages == other.totalPages;
  }
}

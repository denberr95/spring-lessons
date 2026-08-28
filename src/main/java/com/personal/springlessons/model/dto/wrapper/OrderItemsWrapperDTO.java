package com.personal.springlessons.model.dto.wrapper;

import java.util.List;
import java.util.Objects;

import com.personal.springlessons.model.dto.OrderItemsDTO;

import org.jspecify.annotations.Nullable;

public class OrderItemsWrapperDTO {

  private @Nullable List<OrderItemsDTO> content;
  private int page;
  private int size;
  private long totalElements;
  private int totalPages;

  public OrderItemsWrapperDTO() {
    // no-args constructor for MapStruct
  }

  public @Nullable List<OrderItemsDTO> getContent() {
    return this.content;
  }

  public void setContent(@Nullable List<OrderItemsDTO> content) {
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
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof OrderItemsWrapperDTO that))
      return false;
    return this.page == that.page && this.size == that.size
        && this.totalElements == that.totalElements && this.totalPages == that.totalPages
        && Objects.equals(this.content, that.content);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.content, this.page, this.size, this.totalElements, this.totalPages);
  }

  @Override
  public String toString() {
    return "OrderItemsWrapperDTO{" + "content=" + this.content + ", page=" + this.page + ", size="
        + this.size + ", totalElements=" + this.totalElements + ", totalPages=" + this.totalPages
        + '}';
  }
}

package com.personal.springlessons.model.dto.wrapper;

import java.util.List;
import java.util.Objects;

import com.personal.springlessons.model.dto.BookDTO;

import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;

public class BooksWrapperDTO {

  private @Nullable BookDTO bookDTO;
  private @Nullable List<BookDTO> bookDTOs;
  private @Nullable MultiValueMap<String, String> httpHeaders;

  public BooksWrapperDTO() {
    // no-args constructor for MapStruct
  }

  public @Nullable BookDTO getBookDTO() {
    return this.bookDTO;
  }

  public void setBookDTO(@Nullable BookDTO bookDTO) {
    this.bookDTO = bookDTO;
  }

  public @Nullable List<BookDTO> getBookDTOs() {
    return this.bookDTOs;
  }

  public void setBookDTOs(@Nullable List<BookDTO> bookDTOs) {
    this.bookDTOs = bookDTOs;
  }

  public @Nullable MultiValueMap<String, String> getHttpHeaders() {
    return this.httpHeaders;
  }

  public void setHttpHeaders(@Nullable MultiValueMap<String, String> httpHeaders) {
    this.httpHeaders = httpHeaders;
  }

  public HttpHeaders convertHttpHeaders() {
    return this.httpHeaders != null ? new HttpHeaders(this.httpHeaders) : HttpHeaders.EMPTY;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof BooksWrapperDTO that))
      return false;
    return Objects.equals(this.bookDTO, that.bookDTO)
        && Objects.equals(this.bookDTOs, that.bookDTOs)
        && Objects.equals(this.httpHeaders, that.httpHeaders);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.bookDTO, this.bookDTOs, this.httpHeaders);
  }

  @Override
  public String toString() {
    return "BooksWrapperDTO{" + "bookDTO=" + this.bookDTO + ", bookDTOs=" + this.bookDTOs
        + ", httpHeaders=" + this.httpHeaders + '}';
  }
}

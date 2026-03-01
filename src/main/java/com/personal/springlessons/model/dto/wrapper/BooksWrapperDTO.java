package com.personal.springlessons.model.dto.wrapper;

import java.util.List;

import com.personal.springlessons.model.dto.BookDTO;

import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BooksWrapperDTO {

  private BookDTO bookDTO;
  private List<BookDTO> bookDTOs;
  private MultiValueMap<String, String> httpHeaders;

  public HttpHeaders convertHttpHeaders() {
    return this.httpHeaders != null ? new HttpHeaders(this.httpHeaders) : HttpHeaders.EMPTY;
  }
}

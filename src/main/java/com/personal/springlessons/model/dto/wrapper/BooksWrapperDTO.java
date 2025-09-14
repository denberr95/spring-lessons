package com.personal.springlessons.model.dto.wrapper;

import java.util.List;
import java.util.Objects;
import com.personal.springlessons.model.dto.BookDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class BooksWrapperDTO {

    private BookDTO bookDTO;
    private List<BookDTO> bookDTOs;
    private MultiValueMap<String, String> httpHeaders;

    public BooksWrapperDTO() {
        this.httpHeaders = new LinkedMultiValueMap<>();
    }

    public BooksWrapperDTO(BookDTO bookDTO, MultiValueMap<String, String> httpHeaders) {
        this.bookDTO = bookDTO;
        this.httpHeaders = httpHeaders != null ? new LinkedMultiValueMap<>(httpHeaders)
                : new LinkedMultiValueMap<>();
    }

    public BooksWrapperDTO(List<BookDTO> bookDTOs, MultiValueMap<String, String> httpHeaders) {
        this.bookDTOs = bookDTOs;
        this.httpHeaders = httpHeaders != null ? new LinkedMultiValueMap<>(httpHeaders)
                : new LinkedMultiValueMap<>();
    }

    public BookDTO getBookDTO() {
        return this.bookDTO;
    }

    public void setBookDTO(BookDTO bookDTO) {
        this.bookDTO = bookDTO;
    }

    public List<BookDTO> getBookDTOs() {
        return this.bookDTOs;
    }

    public void setBookDTOs(List<BookDTO> bookDTOs) {
        this.bookDTOs = bookDTOs;
    }

    public MultiValueMap<String, String> getHttpHeaders() {
        return this.httpHeaders;
    }

    public void setHttpHeaders(MultiValueMap<String, String> httpHeaders) {
        this.httpHeaders = httpHeaders != null ? new LinkedMultiValueMap<>(httpHeaders)
                : new LinkedMultiValueMap<>();
    }

    public HttpHeaders convertHttpHeaders() {
        return this.httpHeaders != null ? new HttpHeaders(this.httpHeaders) : HttpHeaders.EMPTY;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || this.getClass() != obj.getClass())
            return false;
        BooksWrapperDTO other = (BooksWrapperDTO) obj;
        return Objects.equals(this.bookDTO, other.bookDTO)
                && Objects.equals(this.bookDTOs, other.bookDTOs)
                && Objects.equals(this.httpHeaders, other.httpHeaders);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.bookDTO, this.bookDTOs, this.httpHeaders);
    }

    @Override
    public String toString() {
        return "BooksWrapperDTO [bookDTO=" + this.bookDTO + ", bookDTOs=" + this.bookDTOs
                + ", httpHeaders=" + this.httpHeaders + "]";
    }
}

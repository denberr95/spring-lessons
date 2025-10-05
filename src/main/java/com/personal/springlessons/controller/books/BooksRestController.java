package com.personal.springlessons.controller.books;

import java.util.List;

import com.personal.springlessons.model.dto.BookDTO;
import com.personal.springlessons.model.dto.DownloadFileDTO;
import com.personal.springlessons.model.dto.wrapper.BooksWrapperDTO;
import com.personal.springlessons.model.lov.Channel;
import com.personal.springlessons.service.books.BooksService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.micrometer.observation.annotation.Observed;
import io.micrometer.tracing.annotation.SpanTag;

@RestController
public class BooksRestController implements IBooksRestController {

  private static final Logger log = LoggerFactory.getLogger(BooksRestController.class);
  private final BooksService bookService;

  public BooksRestController(BooksService bookService) {
    this.bookService = bookService;
  }

  @Observed(name = "books.get.all", contextualName = "get-all-books",
      lowCardinalityKeyValues = {"endpoint", "/books"})
  @PreAuthorize(value = "hasAuthority('SCOPE_books:get')")
  @Override
  public ResponseEntity<List<BookDTO>> getAll() {
    log.info("Called API to retrieve all books");
    BooksWrapperDTO result = this.bookService.getAll();
    ResponseEntity<List<BookDTO>> response;
    if (result.getBookDTOs().isEmpty()) {
      log.info("No books retrieved !");
      response =
          ResponseEntity.status(HttpStatus.NO_CONTENT).headers(result.convertHttpHeaders()).build();
    } else {
      log.info("Books retrieved !");
      response = ResponseEntity.status(HttpStatus.OK).headers(result.convertHttpHeaders())
          .body(result.getBookDTOs());
    }
    return response;
  }

  @Observed(name = "get.book.by.id", contextualName = "book-id-retrieval",
      lowCardinalityKeyValues = {"endpoint", "/books/{id}"})
  @PreAuthorize(value = "hasAuthority('SCOPE_books:get')")
  @Override
  public ResponseEntity<BookDTO> getById(@SpanTag final String id) {
    log.info("Called API to retrieve book: '{}'", id);
    BooksWrapperDTO result = this.bookService.getById(id);
    log.info("Book '{}' retrieved !", result.getBookDTO().id());
    return ResponseEntity.status(HttpStatus.OK).headers(result.convertHttpHeaders())
        .body(result.getBookDTO());
  }

  @Observed(name = "create.book", contextualName = "book-creation",
      lowCardinalityKeyValues = {"endpoint", "/books"})
  @PreAuthorize(value = "hasAuthority('SCOPE_books:save')")
  @Override
  public ResponseEntity<BookDTO> save(final BookDTO bookDTO, final Channel channel) {
    log.info("Called API to create new book");
    BooksWrapperDTO result = this.bookService.save(bookDTO, channel);
    log.info("Book '{}' saved !", result.getBookDTO().id());
    return ResponseEntity.status(HttpStatus.CREATED).headers(result.convertHttpHeaders())
        .body(result.getBookDTO());
  }

  @Observed(name = "delete.book", contextualName = "book-deletion",
      lowCardinalityKeyValues = {"endpoint", "/books/{id}"})
  @PreAuthorize(value = "hasAuthority('SCOPE_books:delete')")
  @Override
  public ResponseEntity<Void> delete(@SpanTag final String id, @SpanTag final String ifMatch) {
    log.info("Called API to delete book: '{}'", id);
    this.bookService.delete(id, ifMatch);
    log.info("Book '{}' deleted !", id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @Observed(name = "update.book", contextualName = "book-update",
      lowCardinalityKeyValues = {"endpoint", "/books/{id}"})
  @PreAuthorize(value = "hasAuthority('SCOPE_books:update')")
  @Override
  public ResponseEntity<BookDTO> update(@SpanTag final String id, final BookDTO bookDTO,
      final Channel channel, @SpanTag final String ifMatch) {
    log.info("Called API to update book: '{}'", id);
    BooksWrapperDTO result = this.bookService.update(id, bookDTO, channel, ifMatch);
    log.info("Book '{}' updated !", result.getBookDTO().id());
    return ResponseEntity.status(HttpStatus.OK).headers(result.convertHttpHeaders())
        .body(result.getBookDTO());
  }

  @Observed(name = "download.books", contextualName = "books-download",
      lowCardinalityKeyValues = {"endpoint", "/books/download"})
  @PreAuthorize(value = "hasAuthority('SCOPE_books:download')")
  @Override
  public ResponseEntity<byte[]> download() {
    log.info("Called API to download books");
    DownloadFileDTO result = this.bookService.download();
    HttpHeaders headers = new HttpHeaders();
    headers.setContentDisposition(
        ContentDisposition.builder("attachment").filename(result.getFileName()).build());
    log.info("Books csv file '{}' created !", result.getFileName());
    return ResponseEntity.status(HttpStatus.OK).headers(headers).body(result.getContent());
  }

  @Observed(name = "upload.books", contextualName = "books-upload",
      lowCardinalityKeyValues = {"endpoint", "/books/upload"})
  @PreAuthorize(value = "hasAuthority('SCOPE_books:upload')")
  @Override
  public ResponseEntity<Void> upload(final Channel channel, MultipartFile multipartFile) {
    log.info("Called API to upload csv books: '{}'", multipartFile.getOriginalFilename());
    this.bookService.upload(channel, multipartFile);
    log.info("Upload csv file: '{}' completed !", multipartFile.getOriginalFilename());
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}

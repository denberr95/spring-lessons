package com.personal.springlessons.controller;

import java.util.List;
import com.personal.springlessons.model.dto.BookDTO;
import com.personal.springlessons.service.BooksService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.micrometer.tracing.annotation.NewSpan;
import io.micrometer.tracing.annotation.SpanTag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "books")
public class BooksRestController {

    private final BooksService bookService;

    @NewSpan
    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_books:read')")
    public ResponseEntity<List<BookDTO>> getAll() {
        log.info("Called API to retrieve all books");
        List<BookDTO> result = this.bookService.getAll();
        ResponseEntity<List<BookDTO>> response;
        if (result.isEmpty()) {
            log.info("No books retrieved !");
            response = ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            log.info("Books retrieved !");
            response = ResponseEntity.status(HttpStatus.OK).body(result);
        }
        return response;
    }

    @NewSpan
    @GetMapping(path = "{id}")
    @PreAuthorize("hasAuthority('SCOPE_books:read')")
    public ResponseEntity<BookDTO> getById(@SpanTag @PathVariable final String id) {
        log.info("Called API to retrieve book: '{}'", id);
        BookDTO result = this.bookService.getById(id);
        log.info("Book '{}' retrieved !", result.getId());
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @NewSpan
    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_books:write')")
    public ResponseEntity<BookDTO> save(@RequestBody final BookDTO bookDTO) {
        log.info("Called API to create new book");
        BookDTO result = this.bookService.save(bookDTO);
        log.info("Book '{}' saved !", result.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @NewSpan
    @DeleteMapping(path = "{id}")
    @PreAuthorize("hasAuthority('SCOPE_books:write')")
    public ResponseEntity<Void> delete(@SpanTag @PathVariable final String id) {
        log.info("Called API to delete book: '{}'", id);
        this.bookService.delete(id);
        log.info("Book '{}' deleted !", id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @NewSpan
    @PutMapping(path = "{id}")
    @PreAuthorize("hasAuthority('SCOPE_books:write')")
    public ResponseEntity<BookDTO> update(@SpanTag @PathVariable final String id,
            @RequestBody final BookDTO bookDTO) {
        log.info("Called API to update book: '{}'", id);
        BookDTO result = this.bookService.update(id, bookDTO);
        log.info("Book '{}' updated !", id);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}

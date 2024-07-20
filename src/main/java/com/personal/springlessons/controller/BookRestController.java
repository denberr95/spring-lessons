package com.personal.springlessons.controller;

import java.util.List;
import com.personal.springlessons.model.dto.BookDTO;
import com.personal.springlessons.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
public class BookRestController {

    private final BookService bookService;

    @NewSpan
    @GetMapping
    public ResponseEntity<List<BookDTO>> getAll() {
        List<BookDTO> result = null;
        log.info("Called API to retrieve all books");
        result = this.bookService.getAll();
        log.info("Books retrieved !");
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @NewSpan
    @GetMapping(path = "{id}")
    public ResponseEntity<BookDTO> getById(@PathVariable String id) {
        BookDTO result = null;
        log.info("Called API to retrieve book: '{}'", id);
        result = this.bookService.getById(id);
        log.info("Book '{}' retrieved !", result.getId());
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @NewSpan
    @PostMapping
    public ResponseEntity<BookDTO> save(@RequestBody BookDTO bookDTO) {
        BookDTO result = null;
        result = this.bookService.save(bookDTO);
        log.info("Book '{}' saved !", result.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @NewSpan
    @DeleteMapping(path = "{id}")
    public ResponseEntity<Void> delete(@SpanTag @PathVariable String id) {
        log.info("Called API to delete book: '{}'", id);
        this.bookService.delete(id);
        log.info("Book '{}' deleted !", id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

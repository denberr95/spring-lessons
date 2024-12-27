package com.personal.springlessons.controller.books;

import java.util.List;
import com.personal.springlessons.model.dto.BookDTO;
import com.personal.springlessons.model.dto.response.BookNotFoundResponseDTO;
import com.personal.springlessons.model.dto.response.DuplicatedBookResponseDTO;
import com.personal.springlessons.model.dto.response.GenericErrorResponseDTO;
import com.personal.springlessons.model.dto.response.InvalidUUIDResponseDTO;
import com.personal.springlessons.service.BooksService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "Books")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "books")
public class BooksRestController {

    private final BooksService bookService;

    @NewSpan
    @GetMapping
    @PreAuthorize(value = "hasAuthority('SCOPE_books:get')")
    @Operation(summary = "Get all books", operationId = "getAllBooks")
    @ApiResponse(responseCode = "200", description = "OK",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = BookDTO.class)))})
    @ApiResponse(responseCode = "204", description = "No Content",
            content = {@Content(schema = @Schema(implementation = Void.class))})
    @ApiResponse(responseCode = "500", description = "Internal Server Error",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = GenericErrorResponseDTO.class))})
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
    @PreAuthorize(value = "hasAuthority('SCOPE_books:get')")
    @Operation(summary = "Get a book by id", operationId = "getBookById")
    @ApiResponse(responseCode = "200", description = "OK",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = BookDTO.class))})
    @ApiResponse(responseCode = "400", description = "Invalid UUID",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = InvalidUUIDResponseDTO.class))})
    @ApiResponse(responseCode = "404", description = "Book not found",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = BookNotFoundResponseDTO.class))})
    @ApiResponse(responseCode = "500", description = "Internal Server Error",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = GenericErrorResponseDTO.class))})
    public ResponseEntity<BookDTO> getById(@SpanTag @PathVariable final String id) {
        log.info("Called API to retrieve book: '{}'", id);
        BookDTO result = this.bookService.getById(id);
        log.info("Book '{}' retrieved !", result.getId());
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @NewSpan
    @PostMapping
    @PreAuthorize(value = "hasAuthority('SCOPE_books:save')")
    @Operation(summary = "Create a new book", operationId = "createBook")
    @ApiResponse(responseCode = "201", description = "Created",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = BookDTO.class))})
    @ApiResponse(responseCode = "400", description = "Invalid UUID",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = InvalidUUIDResponseDTO.class))})
    @ApiResponse(responseCode = "409", description = "Book already exists",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = DuplicatedBookResponseDTO.class))})
    @ApiResponse(responseCode = "500", description = "Internal Server Error",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = GenericErrorResponseDTO.class))})
    public ResponseEntity<BookDTO> save(@RequestBody final BookDTO bookDTO) {
        log.info("Called API to create new book");
        BookDTO result = this.bookService.save(bookDTO);
        log.info("Book '{}' saved !", result.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @NewSpan
    @DeleteMapping(path = "{id}")
    @PreAuthorize(value = "hasAuthority('SCOPE_books:delete')")
    @Operation(summary = "Delete a book", operationId = "deleteBook")
    @ApiResponse(responseCode = "204", description = "No Content",
            content = {@Content(schema = @Schema(implementation = Void.class))})
    @ApiResponse(responseCode = "400", description = "Invalid UUID",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = InvalidUUIDResponseDTO.class))})
    @ApiResponse(responseCode = "404", description = "Book not found",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = BookNotFoundResponseDTO.class))})
    @ApiResponse(responseCode = "500", description = "Internal Server Error",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = GenericErrorResponseDTO.class))})
    public ResponseEntity<Void> delete(@SpanTag @PathVariable final String id) {
        log.info("Called API to delete book: '{}'", id);
        this.bookService.delete(id);
        log.info("Book '{}' deleted !", id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @NewSpan
    @PutMapping(path = "{id}")
    @PreAuthorize(value = "hasAuthority('SCOPE_books:update')")
    @Operation(summary = "Delete a book", operationId = "updateBook")
    @ApiResponse(responseCode = "200", description = "OK",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = BookDTO.class))})
    @ApiResponse(responseCode = "400", description = "Invalid UUID",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = InvalidUUIDResponseDTO.class))})
    @ApiResponse(responseCode = "404", description = "Book not found",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = BookNotFoundResponseDTO.class))})
    @ApiResponse(responseCode = "409", description = "Book already exists",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = DuplicatedBookResponseDTO.class))})
    @ApiResponse(responseCode = "500", description = "Internal Server Error",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = GenericErrorResponseDTO.class))})
    public ResponseEntity<BookDTO> update(@SpanTag @PathVariable final String id,
            @RequestBody final BookDTO bookDTO) {
        log.info("Called API to update book: '{}'", id);
        BookDTO result = this.bookService.update(id, bookDTO);
        log.info("Book '{}' updated !", id);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}

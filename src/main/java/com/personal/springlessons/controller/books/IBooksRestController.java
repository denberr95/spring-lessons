package com.personal.springlessons.controller.books;

import java.util.List;

import jakarta.validation.Valid;

import com.personal.springlessons.model.dto.BookDTO;
import com.personal.springlessons.model.dto.response.BookNotFoundResponseDTO;
import com.personal.springlessons.model.dto.response.ConcurrentUpdateResponseDTO;
import com.personal.springlessons.model.dto.response.DuplicatedBookResponseDTO;
import com.personal.springlessons.model.dto.response.GenericErrorResponseDTO;
import com.personal.springlessons.model.dto.response.InvalidArgumentTypeResponseDTO;
import com.personal.springlessons.model.dto.response.InvalidCSVContentResponseDTO;
import com.personal.springlessons.model.dto.response.InvalidFileTypeResponseDTO;
import com.personal.springlessons.model.dto.response.InvalidUUIDResponseDTO;
import com.personal.springlessons.model.dto.response.MissingHttpRequestHeaderResponseDTO;
import com.personal.springlessons.model.dto.response.NotReadableBodyRequestResponseDTO;
import com.personal.springlessons.model.dto.response.ValidationRequestErrorResponseDTO;
import com.personal.springlessons.model.lov.Channel;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Books V1")
@Validated
@RequestMapping(path = "/v1/books")
public interface IBooksRestController {

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Get all books", operationId = "getAllBooksV1")
  @ApiResponse(responseCode = "200", description = "OK",
      content = {@Content(array = @ArraySchema(schema = @Schema(implementation = BookDTO.class)))})
  @ApiResponse(responseCode = "204", description = "No Content",
      content = {@Content(schema = @Schema(implementation = Void.class))})
  @ApiResponse(responseCode = "500", description = "Internal Server Error",
      content = {@Content(schema = @Schema(implementation = GenericErrorResponseDTO.class))})
  ResponseEntity<List<BookDTO>> getAll();

  @GetMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Get a book by id", operationId = "getBookByIdV1")
  @ApiResponse(responseCode = "200", description = "OK",
      content = {@Content(schema = @Schema(implementation = BookDTO.class))})
  @ApiResponse(responseCode = "400", description = "Bad Request",
      content = {@Content(schema = @Schema(implementation = InvalidUUIDResponseDTO.class))})
  @ApiResponse(responseCode = "404", description = "Not Found",
      content = {@Content(schema = @Schema(implementation = BookNotFoundResponseDTO.class))})
  @ApiResponse(responseCode = "500", description = "Internal Server Error",
      content = {@Content(schema = @Schema(implementation = GenericErrorResponseDTO.class))})
  ResponseEntity<BookDTO> getById(@PathVariable final String id);

  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Create a new book", operationId = "createBookV1")
  @ApiResponse(responseCode = "201", description = "Created",
      content = {@Content(schema = @Schema(implementation = BookDTO.class))})
  @ApiResponse(responseCode = "400", description = "Bad Request",
      content = {@Content(schema = @Schema(oneOf = {InvalidUUIDResponseDTO.class,
          MissingHttpRequestHeaderResponseDTO.class, InvalidArgumentTypeResponseDTO.class,
          ValidationRequestErrorResponseDTO.class, NotReadableBodyRequestResponseDTO.class}))})
  @ApiResponse(responseCode = "409", description = "Conflict",
      content = {@Content(schema = @Schema(implementation = DuplicatedBookResponseDTO.class))})
  @ApiResponse(responseCode = "500", description = "Internal Server Error",
      content = {@Content(schema = @Schema(implementation = GenericErrorResponseDTO.class))})
  ResponseEntity<BookDTO> save(@Valid @RequestBody final BookDTO bookDTO,
      @RequestHeader final Channel channel);

  @DeleteMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Delete a book", operationId = "deleteBookV1")
  @ApiResponse(responseCode = "204", description = "No Content",
      content = {@Content(schema = @Schema(implementation = Void.class))})
  @ApiResponse(responseCode = "400", description = "Bad Request",
      content = {@Content(schema = @Schema(implementation = InvalidUUIDResponseDTO.class))})
  @ApiResponse(responseCode = "404", description = "Not Found",
      content = {@Content(schema = @Schema(implementation = BookNotFoundResponseDTO.class))})
  @ApiResponse(responseCode = "409", description = "Conflict",
      content = {@Content(schema = @Schema(implementation = ConcurrentUpdateResponseDTO.class))})
  @ApiResponse(responseCode = "500", description = "Internal Server Error",
      content = {@Content(schema = @Schema(implementation = GenericErrorResponseDTO.class))})
  ResponseEntity<Void> delete(@PathVariable final String id,
      @RequestHeader(value = "If-Match") final String ifMatch);

  @PutMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Update a book", operationId = "updateBookV1")
  @ApiResponse(responseCode = "200", description = "OK",
      content = {@Content(schema = @Schema(implementation = BookDTO.class))})
  @ApiResponse(responseCode = "400", description = "Bad Request",
      content = {@Content(schema = @Schema(oneOf = {InvalidUUIDResponseDTO.class,
          MissingHttpRequestHeaderResponseDTO.class, InvalidArgumentTypeResponseDTO.class,
          ValidationRequestErrorResponseDTO.class, NotReadableBodyRequestResponseDTO.class}))})
  @ApiResponse(responseCode = "404", description = "Not Found",
      content = {@Content(schema = @Schema(implementation = BookNotFoundResponseDTO.class))})
  @ApiResponse(responseCode = "409", description = "Conflict",
      content = {@Content(schema = @Schema(
          oneOf = {ConcurrentUpdateResponseDTO.class, DuplicatedBookResponseDTO.class}))})
  @ApiResponse(responseCode = "500", description = "Internal Server Error",
      content = {@Content(schema = @Schema(implementation = GenericErrorResponseDTO.class))})
  ResponseEntity<BookDTO> update(@PathVariable final String id,
      @Valid @RequestBody final BookDTO bookDTO, @RequestHeader final Channel channel,
      @RequestHeader(value = "If-Match") final String ifMatch);

  @GetMapping(path = "download")
  @Operation(summary = "Download books in a csv file", operationId = "downloadBooksV1")
  @ApiResponse(responseCode = "200", description = "OK",
      content = {@Content(schema = @Schema(implementation = byte.class),
          mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE)})
  @ApiResponse(responseCode = "500", description = "Internal Server Error",
      content = {@Content(schema = @Schema(implementation = GenericErrorResponseDTO.class),
          mediaType = MediaType.APPLICATION_JSON_VALUE)})
  ResponseEntity<byte[]> download();

  @PostMapping(path = "upload", produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @Operation(summary = "Upload books from a csv file", operationId = "uploadBooksV1")
  @ApiResponse(responseCode = "204", description = "No Content",
      content = {@Content(schema = @Schema(implementation = Void.class))})
  @ApiResponse(responseCode = "400", description = "Bad Request",
      content = {@Content(schema = @Schema(
          oneOf = {InvalidFileTypeResponseDTO.class, InvalidCSVContentResponseDTO.class}))})
  @ApiResponse(responseCode = "500", description = "Internal Server Error",
      content = {@Content(schema = @Schema(implementation = GenericErrorResponseDTO.class))})
  ResponseEntity<Void> upload(@RequestHeader final Channel channel,
      @RequestPart(name = "file") MultipartFile multipartFile);
}

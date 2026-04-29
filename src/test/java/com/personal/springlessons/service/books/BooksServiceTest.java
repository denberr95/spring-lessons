package com.personal.springlessons.service.books;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.UUID;

import com.personal.springlessons.exception.ConcurrentUpdateException;
import com.personal.springlessons.exception.InvalidUUIDException;
import com.personal.springlessons.exception.books.BookNotFoundException;
import com.personal.springlessons.exception.books.CSVContentValidationException;
import com.personal.springlessons.exception.books.DuplicatedBookException;
import com.personal.springlessons.exception.books.InvalidFileTypeException;
import com.personal.springlessons.model.dto.BookDTO;
import com.personal.springlessons.model.dto.DownloadFileDTO;
import com.personal.springlessons.model.dto.platformhistory.GetBookHistoryRequest;
import com.personal.springlessons.model.dto.platformhistory.GetBookHistoryResponse;
import com.personal.springlessons.model.dto.platformhistory.ResultType;
import com.personal.springlessons.model.dto.wrapper.BooksWrapperDTO;
import com.personal.springlessons.model.lov.Channel;
import com.personal.springlessons.model.lov.Genre;
import com.personal.springlessons.repository.books.IBooksRepository;
import com.personal.springlessons.util.Methods;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

@SpringBootTest
class BooksServiceTest {

  @Autowired
  private BooksService booksService;

  @Autowired
  private IBooksRepository booksRepository;

  @AfterEach
  void cleanup() {
    this.booksRepository.deleteAll();
  }

  private BookDTO createValidBookDTO(String name) {
    return new BookDTO(null, name, 100, LocalDate.of(2024, 1, 1), Genre.NOIR);
  }

  @Test
  void givenBooks_whenGetAll_thenReturnBooksWithTotalHeader() {
    this.booksService.save(this.createValidBookDTO("Book One"), Channel.NA);
    this.booksService.save(this.createValidBookDTO("Book Two"), Channel.NA);

    BooksWrapperDTO result = this.booksService.getAll();

    assertFalse(result.getBookDTOs().isEmpty());
    assertNotNull(result.getHttpHeaders().getFirst("X-Total-Records"));
  }

  @Test
  void givenEmptyDatabase_whenGetAll_thenReturnEmptyListWithZeroTotal() {
    BooksWrapperDTO result = this.booksService.getAll();

    assertTrue(result.getBookDTOs().isEmpty());
    assertEquals("0", result.getHttpHeaders().getFirst("X-Total-Records"));
  }

  @Test
  void givenExistingId_whenGetById_thenReturnBookWithEtag() {
    BooksWrapperDTO saved =
        this.booksService.save(this.createValidBookDTO("Test Book"), Channel.NA);
    String id = saved.getBookDTO().id();

    BooksWrapperDTO result = this.booksService.getById(id);

    assertNotNull(result.getBookDTO());
    assertNotNull(result.getHttpHeaders().getFirst("ETag"));
  }

  @Test
  void givenNonExistingId_whenGetById_thenThrowBookNotFoundException() {
    assertThrows(BookNotFoundException.class,
        () -> this.booksService.getById(UUID.randomUUID().toString()));
  }

  @Test
  void givenInvalidId_whenGetById_thenThrowInvalidUUIDException() {
    assertThrows(InvalidUUIDException.class, () -> this.booksService.getById("not-a-uuid"));
  }

  @Test
  void givenNewBook_whenSave_thenBookIsPersisted() {
    BooksWrapperDTO result =
        this.booksService.save(this.createValidBookDTO("New Book"), Channel.NA);

    assertNotNull(result.getBookDTO().id());
  }

  @Test
  void givenDuplicatedBook_whenSave_thenThrowDuplicatedBookException() {
    this.booksService.save(this.createValidBookDTO("Duplicate Book"), Channel.NA);

    assertThrows(DuplicatedBookException.class,
        () -> this.booksService.save(this.createValidBookDTO("Duplicate Book"), Channel.NA));
  }

  @Test
  void givenExistingBookWithMatchingEtag_whenDelete_thenBookIsDeleted() {
    BooksWrapperDTO saved =
        this.booksService.save(this.createValidBookDTO("Delete Me"), Channel.NA);
    String id = saved.getBookDTO().id();
    String etag = Methods.getEtag(saved.getHttpHeaders());

    assertDoesNotThrow(() -> this.booksService.delete(id, etag));
    assertThrows(BookNotFoundException.class, () -> this.booksService.getById(id));
  }

  @Test
  void givenNonExistingId_whenDelete_thenThrowBookNotFoundException() {
    assertThrows(BookNotFoundException.class,
        () -> this.booksService.delete(UUID.randomUUID().toString(), "0"));
  }

  @Test
  void givenEtagMismatch_whenDelete_thenThrowConcurrentUpdateException() {
    BooksWrapperDTO saved =
        this.booksService.save(this.createValidBookDTO("ETag Book"), Channel.NA);
    String id = saved.getBookDTO().id();

    assertThrows(ConcurrentUpdateException.class, () -> this.booksService.delete(id, "999"));
  }

  @Test
  void givenExistingBookWithMatchingEtag_whenUpdate_thenBookIsUpdated() {
    BooksWrapperDTO saved = this.booksService.save(this.createValidBookDTO("Book A"), Channel.NA);
    String id = saved.getBookDTO().id();
    String etag = Methods.getEtag(saved.getHttpHeaders());
    BookDTO updated =
        new BookDTO(null, "Book A Updated", 200, LocalDate.of(2025, 1, 1), Genre.FANTASY);

    BooksWrapperDTO result = this.booksService.update(id, updated, Channel.NA, etag);

    assertEquals("Book A Updated", result.getBookDTO().name());
  }

  @Test
  void givenNonExistingId_whenUpdate_thenThrowBookNotFoundException() {
    BookDTO dto = this.createValidBookDTO("Book");

    assertThrows(BookNotFoundException.class,
        () -> this.booksService.update(UUID.randomUUID().toString(), dto, Channel.NA, "0"));
  }

  @Test
  void givenDuplicatedData_whenUpdate_thenThrowDuplicatedBookException() {
    this.booksService.save(this.createValidBookDTO("Original Book"), Channel.NA);
    BooksWrapperDTO saved2 = this.booksService.save(
        new BookDTO(null, "Another Book", 200, LocalDate.of(2025, 1, 1), Genre.FANTASY),
        Channel.NA);
    String id2 = saved2.getBookDTO().id();
    String etag2 = Methods.getEtag(saved2.getHttpHeaders());

    assertThrows(DuplicatedBookException.class, () -> this.booksService.update(id2,
        this.createValidBookDTO("Original Book"), Channel.NA, etag2));
  }

  @Test
  void givenBooks_whenDownload_thenReturnCsvFile() {
    this.booksService.save(this.createValidBookDTO("Download Book"), Channel.NA);

    DownloadFileDTO result = this.booksService.download();

    assertNotNull(result.getFileName());
    assertTrue(result.getContent().length > 0);
  }

  @Test
  void givenValidCsvFile_whenUpload_thenBooksArePersisted() {
    String csv =
        "'NAME';'NUMBER_OF_PAGES';'PUBLICATION_DATE';'GENRE'\n'Book Name File';'1';'2024-01-01';'NOIR'";
    MockMultipartFile file =
        new MockMultipartFile("file", "test.csv", "text/csv", csv.getBytes(StandardCharsets.UTF_8));

    assertDoesNotThrow(() -> this.booksService.upload(Channel.NA, file));
  }

  @Test
  void givenInvalidFileType_whenUpload_thenThrowInvalidFileTypeException() {
    MockMultipartFile file =
        new MockMultipartFile("file", "test.txt", "text/plain", "content".getBytes());

    assertThrows(InvalidFileTypeException.class, () -> this.booksService.upload(Channel.NA, file));
  }

  @Test
  void givenMalformedCsvContent_whenUpload_thenThrowCSVContentValidationException() {
    String csv =
        "'NAME';'NUMBER_OF_PAGES';'PUBLICATION_DATE';'GENRE'\n'Book Name';'1';'9999-12-31';'NOI'";
    MockMultipartFile file =
        new MockMultipartFile("file", "test.csv", "text/csv", csv.getBytes(StandardCharsets.UTF_8));

    assertThrows(CSVContentValidationException.class,
        () -> this.booksService.upload(Channel.NA, file));
  }

  @Test
  void givenExistingBookId_whenGetBookHistory_thenReturnRevisions() {
    BooksWrapperDTO saved =
        this.booksService.save(this.createValidBookDTO("History Book"), Channel.NA);
    String id = saved.getBookDTO().id();
    String etag = Methods.getEtag(saved.getHttpHeaders());
    BookDTO updated =
        new BookDTO(null, "History Book Updated", 200, LocalDate.of(2025, 6, 1), Genre.FANTASY);
    this.booksService.update(id, updated, Channel.NA, etag);

    GetBookHistoryRequest request = new GetBookHistoryRequest();
    request.setBookId(id);
    GetBookHistoryResponse response = this.booksService.getBookHistory(request);

    assertFalse(response.getRevisionsDTO().isEmpty());
  }

  @Test
  void givenNonExistingBookId_whenGetBookHistory_thenReturnEmptyResult() {
    GetBookHistoryRequest request = new GetBookHistoryRequest();
    request.setBookId(UUID.randomUUID().toString());

    GetBookHistoryResponse response = this.booksService.getBookHistory(request);

    assertTrue(response.getRevisionsDTO().isEmpty());
    assertEquals(ResultType.KO, response.getOutcomeDTO().getResult());
  }
}

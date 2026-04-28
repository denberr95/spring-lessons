package com.personal.springlessons.service.books;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BooksServiceTest {

  @Test
  void givenBooks_whenGetAll_thenReturnBooksWithTotalHeader() {
    // TODO
  }

  @Test
  void givenEmptyDatabase_whenGetAll_thenReturnEmptyListWithZeroTotal() {
    // TODO
  }

  @Test
  void givenExistingId_whenGetById_thenReturnBookWithEtag() {
    // TODO
  }

  @Test
  void givenNonExistingId_whenGetById_thenThrowBookNotFoundException() {
    // TODO
  }

  @Test
  void givenInvalidId_whenGetById_thenThrowInvalidUUIDException() {
    // TODO
  }

  @Test
  void givenNewBook_whenSave_thenBookIsPersisted() {
    // TODO
  }

  @Test
  void givenDuplicatedBook_whenSave_thenThrowDuplicatedBookException() {
    // TODO
  }

  @Test
  void givenExistingBookWithMatchingEtag_whenDelete_thenBookIsDeleted() {
    // TODO
  }

  @Test
  void givenNonExistingId_whenDelete_thenThrowBookNotFoundException() {
    // TODO
  }

  @Test
  void givenEtagMismatch_whenDelete_thenThrowConcurrentUpdateException() {
    // TODO
  }

  @Test
  void givenExistingBookWithMatchingEtag_whenUpdate_thenBookIsUpdated() {
    // TODO
  }

  @Test
  void givenNonExistingId_whenUpdate_thenThrowBookNotFoundException() {
    // TODO
  }

  @Test
  void givenDuplicatedData_whenUpdate_thenThrowDuplicatedBookException() {
    // TODO
  }

  @Test
  void givenBooks_whenDownload_thenReturnCsvFile() {
    // TODO
  }

  @Test
  void givenValidCsvFile_whenUpload_thenBooksArePersisted() {
    // TODO
  }

  @Test
  void givenInvalidFileType_whenUpload_thenThrowInvalidFileTypeException() {
    // TODO
  }

  @Test
  void givenMalformedCsvContent_whenUpload_thenThrowCSVContentValidationException() {
    // TODO
  }

  @Test
  void givenExistingBookId_whenGetBookHistory_thenReturnRevisions() {
    // TODO
  }

  @Test
  void givenNonExistingBookId_whenGetBookHistory_thenReturnEmptyResult() {
    // TODO
  }
}

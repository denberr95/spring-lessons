package com.personal.springlessons.service.books;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import jakarta.persistence.OptimisticLockException;
import jakarta.validation.Validator;

import com.personal.springlessons.component.mapper.IBooksMapper;
import com.personal.springlessons.config.AppPropertiesConfig;
import com.personal.springlessons.exception.ConcurrentUpdateException;
import com.personal.springlessons.exception.PreconditionFailedException;
import com.personal.springlessons.exception.SpringLessonsApplicationException;
import com.personal.springlessons.model.dto.BookDTO;
import com.personal.springlessons.model.dto.platformhistory.GetBookHistoryRequest;
import com.personal.springlessons.model.entity.books.BooksEntity;
import com.personal.springlessons.model.lov.Channel;
import com.personal.springlessons.model.lov.Genre;
import com.personal.springlessons.repository.books.IBooksRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.mock.web.MockMultipartFile;

import io.micrometer.observation.ObservationRegistry;

@ExtendWith(MockitoExtension.class)
class BooksServiceUnitTest {

  @Mock
  private IBooksRepository booksRepository;

  @Mock
  private IBooksMapper booksMapper;

  @Mock
  private AppPropertiesConfig appPropertiesConfig;

  @Mock
  private Validator validator;

  private BooksService booksService;

  @BeforeEach
  void setUp() {
    ObservationRegistry observationRegistry = ObservationRegistry.create();
    this.booksService = new BooksService(appPropertiesConfig, validator, booksRepository,
        booksMapper, observationRegistry);
  }

  @Test
  void givenOptimisticLockingFailureOnDelete_whenDelete_thenThrowConcurrentUpdateException() {
    UUID id = UUID.randomUUID();
    BooksEntity entity = new BooksEntity();
    entity.setId(id);
    entity.setVersion(0L);

    String idValue = id.toString();
    when(booksRepository.findById(id)).thenReturn(Optional.of(entity));
    doThrow(OptimisticLockingFailureException.class).when(booksRepository).delete(entity);

    assertThrows(ConcurrentUpdateException.class,
        () -> booksService.delete(idValue, "\"0\""));
  }

  @Test
  void givenOptimisticLockExceptionOnUpdate_whenUpdate_thenThrowConcurrentUpdateException() {
    UUID id = UUID.randomUUID();
    BooksEntity entity = new BooksEntity();
    entity.setId(id);
    entity.setVersion(0L);

    BookDTO bookDTO = new BookDTO(null, "New Title", 100, LocalDate.now(), Genre.NOIR);

    String idValue = id.toString();
    when(booksRepository.findById(id)).thenReturn(Optional.of(entity));
    when(booksRepository.findByNameAndPublicationDateAndNumberOfPages(any(), any(), any()))
        .thenReturn(Optional.empty());
    when(booksMapper.update(any(), any(), any())).thenReturn(entity);
    when(booksRepository.saveAndFlush(entity)).thenThrow(OptimisticLockException.class);

    assertThrows(ConcurrentUpdateException.class,
        () -> booksService.update(idValue, bookDTO, Channel.POSTMAN, "\"0\""));
  }

  @Test
  void givenStaleEtag_whenDelete_thenThrowPreconditionFailedException() {
    UUID id = UUID.randomUUID();
    BooksEntity entity = new BooksEntity();
    entity.setId(id);
    entity.setVersion(0L);

    String idValue = id.toString();
    when(booksRepository.findById(id)).thenReturn(Optional.of(entity));

    assertThrows(PreconditionFailedException.class,
        () -> booksService.delete(idValue, "\"999\""));
  }

  @Test
  void givenStaleEtag_whenUpdate_thenThrowPreconditionFailedException() {
    UUID id = UUID.randomUUID();
    BooksEntity entity = new BooksEntity();
    entity.setId(id);
    entity.setVersion(0L);

    BookDTO bookDTO = new BookDTO(null, "New Title", 100, LocalDate.now(), Genre.NOIR);

    String idValue = id.toString();
    when(booksRepository.findById(id)).thenReturn(Optional.of(entity));
    when(booksRepository.findByNameAndPublicationDateAndNumberOfPages(any(), any(), any()))
        .thenReturn(Optional.empty());

    assertThrows(PreconditionFailedException.class,
        () -> booksService.update(idValue, bookDTO, Channel.POSTMAN, "\"999\""));
  }

  @Test
  void givenRepositoryThrowsOnFindAll_whenDownload_thenThrowSpringLessonsApplicationException() {
    when(booksRepository.findAll()).thenThrow(new RuntimeException("db error"));

    assertThrows(SpringLessonsApplicationException.class, () -> booksService.download());
  }

  @Test
  void givenInputStreamThrows_whenUpload_thenThrowSpringLessonsApplicationException() {
    AppPropertiesConfig.CsvMetadata csvMetadata = mock(AppPropertiesConfig.CsvMetadata.class);
    when(appPropertiesConfig.getCsvMetadata()).thenReturn(csvMetadata);

    MockMultipartFile file =
        new MockMultipartFile("file", "test.csv", "text/csv", "invalid,csv,content\n".getBytes());

    assertThrows(SpringLessonsApplicationException.class,
        () -> booksService.upload(Channel.POSTMAN, file));
  }

  @Test
  void givenRepositoryThrowsOnFindRevisions_whenGetBookHistory_thenThrowSpringLessonsApplicationException() {
    UUID id = UUID.randomUUID();
    when(booksRepository.findRevisions(id)).thenThrow(new RuntimeException("revision error"));

    GetBookHistoryRequest request = new GetBookHistoryRequest();
    request.setBookId(id.toString());

    assertThrows(SpringLessonsApplicationException.class,
        () -> booksService.getBookHistory(request));
  }
}

package com.personal.springlessons.service.books;

import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.OptimisticLockException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.personal.springlessons.component.mapper.IBooksMapper;
import com.personal.springlessons.config.AppPropertiesConfig;
import com.personal.springlessons.exception.ConcurrentUpdateException;
import com.personal.springlessons.exception.SpringLessonsApplicationException;
import com.personal.springlessons.exception.books.BookNotFoundException;
import com.personal.springlessons.exception.books.CSVContentValidationException;
import com.personal.springlessons.exception.books.DuplicatedBookException;
import com.personal.springlessons.exception.books.InvalidFileTypeException;
import com.personal.springlessons.model.csv.BookCsv;
import com.personal.springlessons.model.dto.BookDTO;
import com.personal.springlessons.model.dto.CsvRowValidationDTO;
import com.personal.springlessons.model.dto.DownloadFileDTO;
import com.personal.springlessons.model.dto.InvalidCsvDTO;
import com.personal.springlessons.model.dto.platformhistory.BookRevisionType;
import com.personal.springlessons.model.dto.platformhistory.EntityType;
import com.personal.springlessons.model.dto.platformhistory.GetBookHistoryRequest;
import com.personal.springlessons.model.dto.platformhistory.GetBookHistoryResponse;
import com.personal.springlessons.model.dto.platformhistory.OutcomeType;
import com.personal.springlessons.model.dto.platformhistory.ResultType;
import com.personal.springlessons.model.dto.wrapper.BooksWrapperDTO;
import com.personal.springlessons.model.entity.books.BooksEntity;
import com.personal.springlessons.model.entity.revision.CustomRevisionEntity;
import com.personal.springlessons.model.lov.Channel;
import com.personal.springlessons.repository.books.IBooksRepository;
import com.personal.springlessons.util.Constants;
import com.personal.springlessons.util.Methods;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.history.Revisions;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.annotation.Observed;

@Service
public class BooksService {

  private final AppPropertiesConfig appPropertiesConfig;
  private final Validator validator;
  private final IBooksRepository bookRepository;
  private final IBooksMapper bookMapper;
  private final ObservationRegistry observationRegistry;

  public BooksService(AppPropertiesConfig appPropertiesConfig, Validator validator,
      IBooksRepository bookRepository, IBooksMapper bookMapper,
      ObservationRegistry observationRegistry) {
    this.appPropertiesConfig = appPropertiesConfig;
    this.validator = validator;
    this.bookRepository = bookRepository;
    this.bookMapper = bookMapper;
    this.observationRegistry = observationRegistry;
  }

  @Observed(name = "books.retrieval", contextualName = "get-all-books")
  public BooksWrapperDTO getAll() {
    List<BooksEntity> bookEntities = this.bookRepository.findAll();
    List<BookDTO> httpBody = this.bookMapper.mapDTO(bookEntities);
    MultiValueMap<String, String> httpHeaders = Methods.addTotalRecord(httpBody.size());

    Observation current = this.observationRegistry.getCurrentObservation();
    if (current != null) {
      current.highCardinalityKeyValue(Constants.SPAN_KEY_TOTAL_BOOKS,
          String.valueOf(bookEntities.size()));
    }

    BooksWrapperDTO result = new BooksWrapperDTO();
    result.setBookDTOs(httpBody);
    result.setHttpHeaders(httpHeaders);
    return result;
  }

  @Observed(name = "books.retrieval", contextualName = "get-book-by-id")
  public BooksWrapperDTO getById(final String id) {
    BooksEntity bookEntity = this.bookRepository.findById(Methods.idValidation(id))
        .orElseThrow(() -> new BookNotFoundException(id));
    BookDTO httpBody = this.bookMapper.mapDTO(bookEntity);
    MultiValueMap<String, String> httpHeaders = Methods.addEtag(bookEntity.getVersion());

    BooksWrapperDTO result = new BooksWrapperDTO();
    result.setBookDTO(httpBody);
    result.setHttpHeaders(httpHeaders);
    return result;
  }

  @Observed(name = "books.save", contextualName = "save-book")
  public BooksWrapperDTO save(final BookDTO bookDTO, final Channel channel) {
    this.bookRepository.findByNameAndPublicationDateAndNumberOfPages(bookDTO.name(),
        bookDTO.publicationDate(), bookDTO.numberOfPages()).ifPresent(book -> {
          throw new DuplicatedBookException(book.getName(), book.getId().toString());
        });

    BooksEntity bookEntity = this.bookMapper.mapEntity(bookDTO, channel);
    bookEntity = this.bookRepository.saveAndFlush(bookEntity);

    Observation current = this.observationRegistry.getCurrentObservation();
    if (current != null) {
      current.highCardinalityKeyValue(Constants.SPAN_KEY_ID_BOOKS, bookEntity.getId().toString());
    }

    BookDTO httpBody = this.bookMapper.mapDTO(bookEntity);
    MultiValueMap<String, String> httpHeaders = Methods.addEtag(bookEntity.getVersion());

    BooksWrapperDTO result = new BooksWrapperDTO();
    result.setBookDTO(httpBody);
    result.setHttpHeaders(httpHeaders);
    return result;
  }

  @Observed(name = "books.delete", contextualName = "delete-book")
  public void delete(final String id, final String ifMatch) {
    BooksEntity bookEntity = this.bookRepository.findById(Methods.idValidation(id))
        .orElseThrow(() -> new BookNotFoundException(id));

    boolean etagMatches = bookEntity.getVersion() != null
        && bookEntity.getVersion().toString().equals(Methods.getEtag(ifMatch));

    if (!etagMatches) {
      throw new ConcurrentUpdateException(id, ifMatch);
    }

    try {
      this.bookRepository.delete(bookEntity);
      this.bookRepository.flush();
    } catch (OptimisticLockingFailureException | OptimisticLockException _) {
      throw new ConcurrentUpdateException(id, ifMatch);
    }
  }

  @Observed(name = "books.update", contextualName = "update-book")
  public BooksWrapperDTO update(final String id, final BookDTO bookDTO, final Channel channel,
      final String ifMatch) {
    BooksEntity bookEntity = this.bookRepository.findById(Methods.idValidation(id))
        .orElseThrow(() -> new BookNotFoundException(id));

    this.bookRepository.findByNameAndPublicationDateAndNumberOfPages(bookDTO.name(),
        bookDTO.publicationDate(), bookDTO.numberOfPages()).ifPresent(book -> {
          throw new DuplicatedBookException(book.getName(), book.getId().toString());
        });

    try {
      String version = Methods.getEtag(ifMatch);
      bookEntity = this.bookMapper.update(bookDTO, channel, bookEntity, version);
      bookEntity = this.bookRepository.saveAndFlush(bookEntity);
    } catch (OptimisticLockingFailureException | OptimisticLockException _) {
      throw new ConcurrentUpdateException(id, ifMatch);
    }

    BookDTO httpBody = this.bookMapper.mapDTO(bookEntity);
    MultiValueMap<String, String> httpHeaders = Methods.addEtag(bookEntity.getVersion());

    BooksWrapperDTO result = new BooksWrapperDTO();
    result.setBookDTO(httpBody);
    result.setHttpHeaders(httpHeaders);
    return result;
  }

  @Observed(name = "books.download", contextualName = "download-books")
  public DownloadFileDTO download() {
    DownloadFileDTO result = new DownloadFileDTO();
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

    try (CSVWriter writer =
        new CSVWriter(new OutputStreamWriter(byteArrayOutputStream, StandardCharsets.UTF_8))) {

      List<BooksEntity> dbData = this.bookRepository.findAll();
      List<BookCsv> csvData = this.bookMapper.mapCsv(dbData);
      String file = Methods.generateFileName("books", Constants.CSV_EXT, true);

      HeaderColumnNameMappingStrategy<BookCsv> strategy = new HeaderColumnNameMappingStrategy<>();
      strategy.setType(BookCsv.class);

      StatefulBeanToCsv<BookCsv> beanToCsv = new StatefulBeanToCsvBuilder<BookCsv>(writer)
          .withSeparator(this.appPropertiesConfig.getCsvMetadata().getColumnSeparator())
          .withLineEnd(System.lineSeparator()).withOrderedResults(true)
          .withMappingStrategy(strategy)
          .withQuotechar(this.appPropertiesConfig.getCsvMetadata().getQuoteCharacter())
          .withApplyQuotesToAll(this.appPropertiesConfig.getCsvMetadata().getApplyAllQuotes())
          .build();

      beanToCsv.write(csvData);
      writer.flush();

      result.setFileName(file);
      result.setContent(byteArrayOutputStream.toByteArray());
    } catch (Exception e) {
      throw new SpringLessonsApplicationException(e);
    }
    return result;
  }

  @Observed(name = "books.upload", contextualName = "upload-books")
  public void upload(final Channel channel, final MultipartFile multipartFile) {
    String filename =
        Optional.ofNullable(multipartFile.getResource().getFilename()).orElse("unknown");
    int row = 0;

    this.isPermittedFileType(filename);

    try (InputStreamReader inputStreamReader =
        new InputStreamReader(multipartFile.getInputStream(), StandardCharsets.UTF_8)) {

      HeaderColumnNameMappingStrategy<BookCsv> strategy = new HeaderColumnNameMappingStrategy<>();
      strategy.setType(BookCsv.class);

      CsvToBean<BookCsv> csvToBean = new CsvToBeanBuilder<BookCsv>(inputStreamReader)
          .withOrderedResults(true).withMappingStrategy(strategy)
          .withStrictQuotes(this.appPropertiesConfig.getCsvMetadata().getStrictQuote())
          .withIgnoreEmptyLine(this.appPropertiesConfig.getCsvMetadata().getIgnoreEmptyLines())
          .withSeparator(this.appPropertiesConfig.getCsvMetadata().getColumnSeparator())
          .withQuoteChar(this.appPropertiesConfig.getCsvMetadata().getQuoteCharacter()).build();

      Iterator<BookCsv> iterator = csvToBean.iterator();
      List<InvalidCsvDTO> invalidCsvDTO = new ArrayList<>();
      List<BookDTO> booksDTO = new ArrayList<>();

      this.validateCsvContent(multipartFile, row, iterator, invalidCsvDTO, booksDTO);
      this.persistCsvContent(channel, booksDTO);

    } catch (CSVContentValidationException e) {
      throw e;
    } catch (Exception e) {
      throw new SpringLessonsApplicationException(e);
    }
  }

  @Observed(name = "books.history", contextualName = "get-book-history")
  public GetBookHistoryResponse getBookHistory(GetBookHistoryRequest getBookHistoryRequest) {
    GetBookHistoryResponse result = new GetBookHistoryResponse();
    String bookId = getBookHistoryRequest.getBookId();
    UUID id = Methods.idValidation(bookId);

    Observation current = this.observationRegistry.getCurrentObservation();
    if (current != null) {
      current.highCardinalityKeyValue(Constants.SPAN_KEY_ID_BOOKS, bookId);
    }

    try {
      Revisions<Long, BooksEntity> revisions = this.bookRepository.findRevisions(id);
      List<BookRevisionType> revisionsDTO = new ArrayList<>();

      revisions.forEach(e -> {
        BookRevisionType item = new BookRevisionType();
        CustomRevisionEntity customRevisionEntity =
            (CustomRevisionEntity) e.getMetadata().getDelegate();
        BooksEntity booksEntity = e.getEntity();

        item.setBookId(booksEntity.getId().toString());
        item.setBookName(booksEntity.getName());
        item.setChannel(booksEntity.getChannel().toString());
        item.setNumberOfPages(booksEntity.getNumberOfPages());
        item.setPublicationDate(booksEntity.getPublicationDate());
        item.setCreatedAt(Methods.convertInstantToOffsetDateTime(booksEntity.getCreatedAt()));
        item.setUpdatedAt(Methods.convertInstantToOffsetDateTime(booksEntity.getUpdatedAt()));

        if (booksEntity.getGenre() != null) {
          item.setGenre(booksEntity.getGenre().toString());
        }

        item.setVersion(booksEntity.getVersion());
        item.setRevisionId(customRevisionEntity.getRev());
        item.setTimestamp(
            Methods.convertInstantToOffsetDateTime(customRevisionEntity.getRevtstmp()));
        item.setClientId(customRevisionEntity.getClientId());
        item.setUsername(customRevisionEntity.getUsername());
        item.setHttpMethod(customRevisionEntity.getHttpMethod());
        item.setRequestURI(customRevisionEntity.getRequestUri());
        item.setIpAddress(customRevisionEntity.getIpAddress());

        revisionsDTO.add(item);
      });

      OutcomeType outcomeType = new OutcomeType();
      outcomeType.setResult(this.resolveHistoryResult(revisionsDTO));
      outcomeType.setMessage(this.resolveHistoryMessage(revisionsDTO));
      outcomeType.setEntity(EntityType.BOOKS);
      outcomeType.setTimestamp(OffsetDateTime.now(ZoneOffset.UTC));

      result.setOutcomeDTO(outcomeType);
      result.getRevisionsDTO().addAll(revisionsDTO);
    } catch (Exception e) {
      throw new SpringLessonsApplicationException(e.getMessage(), e);
    }
    return result;
  }

  private void persistCsvContent(final Channel channel, List<BookDTO> booksDTO) {
    Observation.createNotStarted("books.csv.persistence", this.observationRegistry)
        .contextualName("persist-csv-content")
        .lowCardinalityKeyValue(Constants.OPERATION, "persist").observe(() -> {
          List<BooksEntity> entities = new ArrayList<>(booksDTO.size());
          booksDTO.forEach(item -> entities.add(this.bookMapper.mapEntity(item, channel)));
          this.bookRepository.saveAllAndFlush(entities);
        });
  }

  private void validateCsvContent(final MultipartFile multipartFile, int row,
      Iterator<BookCsv> iterator, List<InvalidCsvDTO> invalidCsvDTO, List<BookDTO> booksDTO) {

    Observation obs = Observation.createNotStarted("books.csv.validation", this.observationRegistry)
        .contextualName("validate-csv-content")
        .lowCardinalityKeyValue(Constants.OPERATION, "validate");
    obs.start();

    try {
      while (iterator.hasNext()) {
        row++;
        BookCsv csvRow = iterator.next();
        BookDTO bookDTO = this.bookMapper.mapDTO(csvRow);
        Set<ConstraintViolation<BookDTO>> violations = this.validator.validate(bookDTO);

        if (!violations.isEmpty()) {
          InvalidCsvDTO csvRowInvalid = new InvalidCsvDTO();
          csvRowInvalid.setRow(row);
          List<CsvRowValidationDTO> rowsValidation = new ArrayList<>(violations.size());

          for (ConstraintViolation<BookDTO> violation : violations) {
            CsvRowValidationDTO rowValidation = new CsvRowValidationDTO();
            rowValidation.setField(violation.getPropertyPath().toString());
            rowValidation.setMessage(violation.getMessage());
            rowValidation.setValue(violation.getInvalidValue());
            rowsValidation.add(rowValidation);
          }
          csvRowInvalid.setValidations(rowsValidation);
          invalidCsvDTO.add(csvRowInvalid);
        }
        booksDTO.add(bookDTO);
      }

      obs.highCardinalityKeyValue(Constants.SPAN_KEY_TOTAL_ROWS, String.valueOf(row));

      if (!invalidCsvDTO.isEmpty()) {
        obs.highCardinalityKeyValue(Constants.SPAN_KEY_TOTAL_INVALID_ROWS,
            String.valueOf(invalidCsvDTO.size()));
        throw new CSVContentValidationException(multipartFile.getOriginalFilename(), invalidCsvDTO);
      }
    } catch (Exception e) {
      obs.error(e);
      throw e;
    } finally {
      obs.stop();
    }
  }

  private void isPermittedFileType(final String fileName) {
    Observation.createNotStarted("books.csv.filetype", this.observationRegistry)
        .contextualName("validate-file-type")
        .lowCardinalityKeyValue(Constants.OPERATION, "validate-file-type").observe(() -> {
          List<String> availableFileTypes = List.of(Constants.CSV_EXT);
          if (!Methods.isValidFileType(fileName, availableFileTypes)) {
            throw new InvalidFileTypeException(fileName, availableFileTypes);
          }
        });
  }

  private String resolveHistoryMessage(List<BookRevisionType> revisionsDTO) {
    return revisionsDTO.isEmpty() ? "No history found for the given book ID"
        : "History found for the given book ID";
  }

  private ResultType resolveHistoryResult(List<BookRevisionType> revisionsDTO) {
    return revisionsDTO.isEmpty() ? ResultType.KO : ResultType.OK;
  }
}

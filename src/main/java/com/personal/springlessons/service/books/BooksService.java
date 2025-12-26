package com.personal.springlessons.service.books;

import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.OptimisticLockException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import com.opencsv.CSVReader;
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

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.annotation.NewSpan;

@Service
public class BooksService {

  private final AppPropertiesConfig appPropertiesConfig;
  private final Validator validator;
  private final IBooksRepository bookRepository;
  private final IBooksMapper bookMapper;
  private final Tracer tracer;

  public BooksService(AppPropertiesConfig appPropertiesConfig, Validator validator,
      IBooksRepository bookRepository, IBooksMapper bookMapper, Tracer tracer) {
    this.appPropertiesConfig = appPropertiesConfig;
    this.validator = validator;
    this.bookRepository = bookRepository;
    this.bookMapper = bookMapper;
    this.tracer = tracer;
  }

  @NewSpan
  public BooksWrapperDTO getAll() {
    Span currentSpan = this.tracer.currentSpan();
    BooksWrapperDTO result = new BooksWrapperDTO();
    MultiValueMap<String, String> httpHeaders;

    List<BooksEntity> bookEntities = this.bookRepository.findAll();
    List<BookDTO> httpBody = this.bookMapper.mapDTO(bookEntities);

    httpHeaders = Methods.addTotalRecord(httpBody.size());

    result.setBookDTOs(httpBody);
    result.setHttpHeaders(httpHeaders);

    currentSpan.tag(Constants.SPAN_KEY_TOTAL_BOOKS, String.valueOf(bookEntities.size()))
        .event("Books retrieved");
    return result;
  }

  @NewSpan
  public BooksWrapperDTO getById(final String id) {
    Span currentSpan = this.tracer.currentSpan();
    BooksWrapperDTO result = new BooksWrapperDTO();
    MultiValueMap<String, String> httpHeaders;

    BooksEntity bookEntity = this.bookRepository.findById(Methods.idValidation(id))
        .orElseThrow(() -> new BookNotFoundException(id));
    BookDTO httpBody = this.bookMapper.mapDTO(bookEntity);

    httpHeaders = Methods.addEtag(bookEntity.getVersion());

    result.setBookDTO(httpBody);
    result.setHttpHeaders(httpHeaders);

    currentSpan.event("Book retrieved");
    return result;
  }

  @NewSpan
  public BooksWrapperDTO save(final BookDTO bookDTO, final Channel channel) {
    Span currentSpan = this.tracer.currentSpan();
    BooksWrapperDTO result = new BooksWrapperDTO();
    MultiValueMap<String, String> httpHeaders;

    this.bookRepository.findByNameAndPublicationDateAndNumberOfPages(bookDTO.name(),
        bookDTO.publicationDate(), bookDTO.numberOfPages()).ifPresent(book -> {
          throw new DuplicatedBookException(book.getName(), book.getId().toString());
        });

    BooksEntity bookEntity = this.bookMapper.mapEntity(bookDTO, channel);
    bookEntity = this.bookRepository.saveAndFlush(bookEntity);

    BookDTO httpBody = this.bookMapper.mapDTO(bookEntity);
    httpHeaders = Methods.addEtag(bookEntity.getVersion());

    result.setBookDTO(httpBody);
    result.setHttpHeaders(httpHeaders);

    currentSpan.tag(Constants.SPAN_KEY_ID_BOOKS, bookEntity.getId().toString())
        .event("Book created");
    return result;
  }

  @NewSpan
  public void delete(final String id, final String ifMatch) {
    Span currentSpan = this.tracer.currentSpan();

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
    } catch (OptimisticLockingFailureException | OptimisticLockException e) {
      throw new ConcurrentUpdateException(id, ifMatch);
    }

    currentSpan.event("Book deleted");
  }

  @NewSpan
  public BooksWrapperDTO update(final String id, final BookDTO bookDTO, final Channel channel,
      final String ifMatch) {
    Span currentSpan = this.tracer.currentSpan();
    BooksWrapperDTO result = new BooksWrapperDTO();
    MultiValueMap<String, String> httpHeaders;

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
    } catch (OptimisticLockingFailureException | OptimisticLockException e) {
      throw new ConcurrentUpdateException(id, ifMatch);
    }

    BookDTO httpBody = this.bookMapper.mapDTO(bookEntity);
    httpHeaders = Methods.addEtag(bookEntity.getVersion());

    result.setBookDTO(httpBody);
    result.setHttpHeaders(httpHeaders);

    currentSpan.event("Book updated");
    return result;
  }

  @NewSpan
  public DownloadFileDTO download() {
    Span currentSpan = this.tracer.currentSpan();

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

      currentSpan.tag(Constants.SPAN_KEY_BOOKS_NAME, file)
          .tag(Constants.SPAN_KEY_TOTAL_BOOKS, String.valueOf(csvData.size()))
          .event("Books downloaded");
    } catch (Exception e) {
      throw new SpringLessonsApplicationException(e);
    }
    return result;
  }

  @NewSpan
  public void upload(final Channel channel, final MultipartFile multipartFile) {
    Span currentSpan = this.tracer.currentSpan();
    currentSpan.tag(Constants.SPAN_KEY_FILE_NAME, multipartFile.getOriginalFilename());

    int row = 0;

    this.isPermittedFileType(multipartFile.getOriginalFilename());

    try (CSVReader reader = new CSVReader(
        new InputStreamReader(multipartFile.getInputStream(), StandardCharsets.UTF_8))) {
      HeaderColumnNameMappingStrategy<BookCsv> strategy = new HeaderColumnNameMappingStrategy<>();
      strategy.setType(BookCsv.class);

      CsvToBean<BookCsv> csvToBean = new CsvToBeanBuilder<BookCsv>(reader).withOrderedResults(true)
          .withMappingStrategy(strategy)
          .withStrictQuotes(this.appPropertiesConfig.getCsvMetadata().getStrictQuote())
          .withIgnoreEmptyLine(this.appPropertiesConfig.getCsvMetadata().getIgnoreEmptyLines())
          .withSeparator(this.appPropertiesConfig.getCsvMetadata().getColumnSeparator())
          .withQuoteChar(this.appPropertiesConfig.getCsvMetadata().getQuoteCharacter())
          .withSkipLines(1).build();

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

  @NewSpan
  public GetBookHistoryResponse getBookHistory(GetBookHistoryRequest getBookHistoryRequest) {
    Span currentSpan = this.tracer.currentSpan();
    GetBookHistoryResponse result = new GetBookHistoryResponse();

    String bookId = getBookHistoryRequest.getBookId();
    currentSpan.tag(Constants.SPAN_KEY_ID_BOOKS, bookId);

    UUID id = Methods.idValidation(bookId);

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
        item.setCreatedAt(booksEntity.getCreatedAt());
        item.setUpdatedAt(booksEntity.getUpdatedAt());
        item.setGenre(booksEntity.getGenre().toString());
        item.setVersion(booksEntity.getVersion());

        item.setRevisionId(customRevisionEntity.getRev());
        item.setTimestamp(Instant.ofEpochMilli(customRevisionEntity.getRevtstmp())
            .atZone(ZoneId.systemDefault()).toOffsetDateTime());
        item.setClientId(customRevisionEntity.getClientId());
        item.setUsername(customRevisionEntity.getUsername());
        item.setHttpMethod(customRevisionEntity.getHttpMethod());
        item.setRequestURI(customRevisionEntity.getRequestUri());
        item.setIpAddress(customRevisionEntity.getIpAddress());

        revisionsDTO.add(item);
      });

      OutcomeType outcomeType = new OutcomeType();
      outcomeType.setResult(ResultType.OK);
      outcomeType.setEntity(EntityType.BOOKS);
      outcomeType.setTimestamp(OffsetDateTime.now());

      String msg = revisionsDTO.isEmpty() ? "No history found for the given book ID"
          : "History found for the given book ID";
      outcomeType.setMessage(msg);

      result.setOutcomeDTO(outcomeType);
      result.getRevisionsDTO().addAll(revisionsDTO);
    } catch (Exception e) {
      throw new SpringLessonsApplicationException(e.getMessage(), e);
    }
    currentSpan.event("Book history retrieved");
    return result;
  }

  private void persistCsvContent(final Channel channel, List<BookDTO> booksDTO) {
    Span span = this.tracer.nextSpan().name("persistCsvContent");
    try (Tracer.SpanInScope ws = this.tracer.withSpan(span.start())) {
      List<BooksEntity> entities = new ArrayList<>(booksDTO.size());
      booksDTO.forEach(item -> entities.add(this.bookMapper.mapEntity(item, channel)));
      this.bookRepository.saveAllAndFlush(entities);
      span.event("Saved books into database");
    } finally {
      span.end();
    }
  }

  private void validateCsvContent(final MultipartFile multipartFile, int row,
      Iterator<BookCsv> iterator, List<InvalidCsvDTO> invalidCsvDTO, List<BookDTO> booksDTO) {
    Span span = this.tracer.nextSpan().name("validateCsvContent");

    try (Tracer.SpanInScope ws = this.tracer.withSpan(span.start())) {
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
      if (!invalidCsvDTO.isEmpty()) {
        span.tag(Constants.SPAN_KEY_TOTAL_INVALID_ROWS, invalidCsvDTO.size())
            .event("CSV Content contains invalid rows");
        throw new CSVContentValidationException(multipartFile.getOriginalFilename(), invalidCsvDTO);
      }
    } finally {
      span.tag(Constants.SPAN_KEY_TOTAL_ROWS, row).event("Calculated total CSV rows");
      span.end();
    }
  }

  private void isPermittedFileType(final String fileName) {
    Span span = this.tracer.nextSpan().name("isPermittedFileType");
    try (Tracer.SpanInScope ws = this.tracer.withSpan(span.start())) {
      List<String> availableFileTypes = List.of(Constants.CSV_EXT);
      if (!Methods.isValidFileType(fileName, availableFileTypes)) {
        throw new InvalidFileTypeException(fileName, availableFileTypes);
      }
    } finally {
      span.end();
    }
  }
}

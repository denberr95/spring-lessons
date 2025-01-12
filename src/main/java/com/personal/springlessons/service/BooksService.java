package com.personal.springlessons.service;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.personal.springlessons.component.mapper.IBooksMapper;
import com.personal.springlessons.config.AppPropertiesConfig;
import com.personal.springlessons.exception.BookNotFoundException;
import com.personal.springlessons.exception.DuplicatedBookException;
import com.personal.springlessons.exception.SpringLessonsApplicationException;
import com.personal.springlessons.model.csv.BookCsv;
import com.personal.springlessons.model.dto.BookDTO;
import com.personal.springlessons.model.dto.DownloadBooksDTO;
import com.personal.springlessons.model.entity.books.BooksEntity;
import com.personal.springlessons.model.lov.Channel;
import com.personal.springlessons.repository.IBooksRepository;
import com.personal.springlessons.util.Constants;
import com.personal.springlessons.util.Methods;
import org.springframework.stereotype.Service;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.annotation.NewSpan;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BooksService {

    private final AppPropertiesConfig appPropertiesConfig;
    private final IBooksRepository bookRepository;
    private final IBooksMapper bookMapper;
    private final Tracer tracer;

    @NewSpan
    public List<BookDTO> getAll() {
        List<BooksEntity> bookEntities = this.bookRepository.findAll();
        Span currentSpan = this.tracer.currentSpan();
        currentSpan.tag(Constants.SPAN_KEY_TOTAL_BOOKS, String.valueOf(bookEntities.size()))
                .event("Books retrieved");
        return this.bookMapper.mapDTO(bookEntities);
    }

    @NewSpan
    public BookDTO getById(final String id) {
        BooksEntity bookEntity = this.bookRepository.findById(Methods.idValidation(id))
                .orElseThrow(() -> new BookNotFoundException(id));
        Span currentSpan = this.tracer.currentSpan();
        currentSpan.event("Book retrieved");
        return this.bookMapper.mapDTO(bookEntity);
    }

    @NewSpan
    public BookDTO save(final BookDTO bookDTO, final Channel channel) {
        this.bookRepository
                .findByNameAndPublicationDateAndNumberOfPages(bookDTO.getName(),
                        bookDTO.getPublicationDate(), bookDTO.getNumberOfPages())
                .ifPresent(book -> {
                    throw new DuplicatedBookException(book.getName(), book.getId().toString());
                });
        BooksEntity bookEntity = this.bookMapper.mapEntity(bookDTO, channel);
        bookEntity = this.bookRepository.saveAndFlush(bookEntity);
        Span currentSpan = this.tracer.currentSpan();
        currentSpan.tag(Constants.SPAN_KEY_ID_BOOKS, bookEntity.getId().toString())
                .event("Book created");
        return this.bookMapper.mapDTO(bookEntity);
    }

    @NewSpan
    public void delete(final String id) {
        this.bookRepository.deleteById(Methods.idValidation(id));
        Span currentSpan = this.tracer.currentSpan();
        currentSpan.event("Book deleted");
    }

    @NewSpan
    public BookDTO update(final String id, final BookDTO bookDTO, final Channel channel) {
        BooksEntity bookEntity = this.bookRepository.findById(Methods.idValidation(id))
                .orElseThrow(() -> new BookNotFoundException(id));
        this.bookRepository
                .findByNameAndPublicationDateAndNumberOfPages(bookDTO.getName(),
                        bookDTO.getPublicationDate(), bookDTO.getNumberOfPages())
                .ifPresent(book -> {
                    throw new DuplicatedBookException(book.getName(), book.getId().toString());
                });
        bookEntity = this.bookMapper.update(bookDTO, channel, bookEntity);
        bookEntity = this.bookRepository.saveAndFlush(bookEntity);
        Span currentSpan = this.tracer.currentSpan();
        currentSpan.event("Book updated");
        return this.bookMapper.mapDTO(bookEntity);
    }

    @NewSpan
    public DownloadBooksDTO download() {
        DownloadBooksDTO result = new DownloadBooksDTO();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (Writer writer = new OutputStreamWriter(byteArrayOutputStream)) {
            List<BooksEntity> dbData = this.bookRepository.findAll();
            List<BookCsv> csvData = this.bookMapper.mapCsv(dbData);
            String file = Methods.generateFileName("books", Constants.CSV_EXT, true);
            HeaderColumnNameMappingStrategy<BookCsv> strategy =
                    new HeaderColumnNameMappingStrategy<>();
            strategy.setType(BookCsv.class);
            StatefulBeanToCsv<BookCsv> beanToCsv = new StatefulBeanToCsvBuilder<BookCsv>(writer)
                    .withSeparator(this.appPropertiesConfig.getCsvMetadata().getColumnSeparator())
                    .withQuotechar(this.appPropertiesConfig.getCsvMetadata().getQuoteCharacter())
                    .withMappingStrategy(strategy).build();
            beanToCsv.write(csvData);
            writer.flush();
            result.setFileName(file);
            result.setContent(byteArrayOutputStream.toByteArray());
        } catch (Exception e) {
            throw new SpringLessonsApplicationException(e);
        }
        return result;
    }
}

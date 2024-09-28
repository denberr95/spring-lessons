package com.personal.springlessons.service;

import java.util.List;
import com.personal.springlessons.component.mapper.IBooksMapper;
import com.personal.springlessons.exception.BookNotFoundException;
import com.personal.springlessons.exception.DuplicatedBookException;
import com.personal.springlessons.model.dto.BookDTO;
import com.personal.springlessons.model.entity.BooksEntity;
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
    public BookDTO save(final BookDTO bookDTO) {
        this.bookRepository
                .findByNameAndPublicationDateAndNumberOfPages(bookDTO.getName(),
                        bookDTO.getPublicationDate(), bookDTO.getNumberOfPages())
                .ifPresent(book -> {
                    throw new DuplicatedBookException(book.getName(), book.getId().toString());
                });
        BooksEntity bookEntity = this.bookMapper.mapEntity(bookDTO);
        bookEntity = this.bookRepository.saveAndFlush(bookEntity);
        Span currentSpan = this.tracer.currentSpan();
        currentSpan.tag(Constants.SPAN_KEY_ID_BOOKS, bookEntity.getId().toString()).event("Book created");
        return this.bookMapper.mapDTO(bookEntity);
    }

    @NewSpan
    public void delete(final String id) {
        this.bookRepository.deleteById(Methods.idValidation(id));
        Span currentSpan = this.tracer.currentSpan();
        currentSpan.event("Book deleted");
    }

    @NewSpan
    public BookDTO update(final String id, final BookDTO bookDTO) {
        BooksEntity bookEntity = this.bookRepository.findById(Methods.idValidation(id))
                .orElseThrow(() -> new BookNotFoundException(id));
        this.bookRepository
                .findByNameAndPublicationDateAndNumberOfPages(bookDTO.getName(),
                        bookDTO.getPublicationDate(), bookDTO.getNumberOfPages())
                .ifPresent(book -> {
                    throw new DuplicatedBookException(book.getName(), book.getId().toString());
                });
        bookEntity = this.bookMapper.update(bookDTO, bookEntity);
        bookEntity = this.bookRepository.saveAndFlush(bookEntity);
        Span currentSpan = this.tracer.currentSpan();
        currentSpan.event("Book updated");
        return this.bookMapper.mapDTO(bookEntity);
    }
}

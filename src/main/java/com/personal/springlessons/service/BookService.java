package com.personal.springlessons.service;

import java.util.List;
import java.util.UUID;
import com.personal.springlessons.component.mapper.IBookMapper;
import com.personal.springlessons.exception.BookNotFoundException;
import com.personal.springlessons.exception.DuplicatedBookException;
import com.personal.springlessons.model.dto.BookDTO;
import com.personal.springlessons.model.entity.BookEntity;
import com.personal.springlessons.repository.IBookRepository;
import org.springframework.stereotype.Service;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.annotation.NewSpan;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookService {

    private final IBookRepository bookRepository;
    private final IBookMapper bookMapper;
    private final Tracer tracer;

    @NewSpan
    public List<BookDTO> getAll() {
        List<BookEntity> bookEntities = this.bookRepository.findAll();
        Span currentSpan = this.tracer.currentSpan();
        if (currentSpan != null) {
            currentSpan.tag("total.books", String.valueOf(bookEntities.size()))
                    .event("Books retrieved");
        }
        return this.bookMapper.mapDTO(bookEntities);
    }

    @NewSpan
    public BookDTO getById(final String id) {
        BookEntity bookEntity = this.bookRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new BookNotFoundException(id));
        Span currentSpan = this.tracer.currentSpan();
        if (currentSpan != null) {
            currentSpan.event("Book retrieved");
        }
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
        BookEntity bookEntity = this.bookMapper.mapEntity(bookDTO);
        bookEntity = this.bookRepository.saveAndFlush(bookEntity);
        Span currentSpan = this.tracer.currentSpan();
        if (currentSpan != null) {
            currentSpan.event("Book created").tag("book.id.created", bookEntity.getId().toString());
        }
        return this.bookMapper.mapDTO(bookEntity);
    }

    @NewSpan
    public void delete(final String id) {
        this.bookRepository.deleteById(UUID.fromString(id));
        Span currentSpan = this.tracer.currentSpan();
        if (currentSpan != null) {
            currentSpan.event("Book deleted");
        }
    }

    @NewSpan
    public BookDTO update(final String id, final BookDTO bookDTO) {
        BookEntity bookEntity = this.bookRepository.findById(UUID.fromString(id))
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
        if (currentSpan != null) {
            currentSpan.event("Book updated");
        }
        return this.bookMapper.mapDTO(bookEntity);
    }
}

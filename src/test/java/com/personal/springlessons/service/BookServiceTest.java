package com.personal.springlessons.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import com.personal.springlessons.exception.BookNotFoundException;
import com.personal.springlessons.exception.DuplicatedBookException;
import com.personal.springlessons.model.dto.BookDTO;
import com.personal.springlessons.model.entity.BookEntity;
import com.personal.springlessons.repository.IBookRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BookServiceTest {

    @Autowired
    private BookService bookService;

    @Autowired
    private IBookRepository bookRepository;

    @AfterEach
    void tearDown() {
        this.bookRepository.deleteAll();
    }

    @Test
    void givenExistingBooks_whenGetAll_thenRetrieveBooks() {
        BookEntity book1 = new BookEntity();
        book1.setName("Book1");
        book1.setNumberOfPages(1);
        book1.setPublicationDate(LocalDate.now());

        BookEntity book2 = new BookEntity();
        book2.setName("Book2");
        book2.setNumberOfPages(2);
        book2.setPublicationDate(LocalDate.now());

        this.bookRepository.save(book1);
        this.bookRepository.save(book2);

        List<BookDTO> result = this.bookService.getAll();
        assertEquals(2, result.size());
    }

    @Test
    void givenExistingBookId_whenGetById_theRetrieveBook() {
        BookEntity bookEntity = new BookEntity();
        bookEntity.setName("New Book");
        bookEntity.setNumberOfPages(1);
        bookEntity.setPublicationDate(LocalDate.now());

        this.bookRepository.save(bookEntity);
        BookDTO result = this.bookService.getById(bookEntity.getId().toString());
        assertNotNull(result);
        assertEquals(result.getId(), bookEntity.getId().toString());
        assertEquals(result.getName(), bookEntity.getName());
        assertEquals(result.getPublicationDate(), bookEntity.getPublicationDate());
        assertEquals(result.getNumberOfPages(), bookEntity.getNumberOfPages());
    }

    @Test
    void givenNonExistingBookId_whenGetById_theThrowBookNotFoundException() {
        String fakeId = UUID.randomUUID().toString();
        assertThrows(BookNotFoundException.class, () -> this.bookService.getById(fakeId));
    }

    @Test
    void givenNewBookDTO_whenSave_thenCreateBook() {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setName("New Book 1");
        bookDTO.setPublicationDate(LocalDate.now());
        bookDTO.setNumberOfPages(Integer.valueOf(1));

        BookDTO result = this.bookService.save(bookDTO);

        assertNotNull(result.getId());
        assertEquals(bookDTO.getName(), result.getName());
        assertEquals(bookDTO.getPublicationDate(), result.getPublicationDate());
        assertEquals(bookDTO.getNumberOfPages(), result.getNumberOfPages());
    }

    @Test
    void givenExistingBookDTO_whenSave_thenThrowDuplicatedBookException() {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setName("New Book 2");
        bookDTO.setPublicationDate(LocalDate.now());
        bookDTO.setNumberOfPages(Integer.valueOf(1));

        this.bookService.save(bookDTO);

        assertThrows(DuplicatedBookException.class, () -> this.bookService.save(bookDTO));
    }

    @Test
    void givenValidBookId_whenDelete_thenDeleteBook() {
        BookEntity bookEntity = new BookEntity();
        bookEntity.setName("New Book 3");
        bookEntity.setNumberOfPages(Integer.valueOf(1));
        bookEntity.setPublicationDate(LocalDate.now());

        this.bookRepository.save(bookEntity);
        String id = bookEntity.getId().toString();
        this.bookService.delete(id);

        assertFalse(this.bookRepository.findById(UUID.fromString(id)).isPresent());
    }
}

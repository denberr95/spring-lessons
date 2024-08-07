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
import com.personal.springlessons.exception.InvalidUUIDException;
import com.personal.springlessons.model.dto.BookDTO;
import com.personal.springlessons.model.entity.BooksEntity;
import com.personal.springlessons.repository.IBooksRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BooksServiceTest {

    @Autowired
    private BooksService bookService;

    @Autowired
    private IBooksRepository bookRepository;

    private static final int TOTAL_BOOKS = 5;

    @BeforeEach
    void init() {
        for (int i = 0; i < TOTAL_BOOKS; i++) {
            BooksEntity bookEntity = new BooksEntity();
            bookEntity.setName("Service-Book-Name-" + i);
            bookEntity.setPublicationDate(LocalDate.now());
            bookEntity.setNumberOfPages(i + 1);
            this.bookRepository.save(bookEntity);
        }
    }

    @AfterEach
    void tearDown() {
        this.bookRepository.deleteAll();
    }

    @Test
    void givenExistingBooks_whenGetAll_thenRetrieveBooks() {
        List<BookDTO> result = this.bookService.getAll();
        assertEquals(TOTAL_BOOKS, result.size());
    }

    @Test
    void givenExistingBookId_whenGetById_theRetrieveBook() {
        BooksEntity bookEntity = this.bookRepository.findAll().get(0);
        BookDTO result = this.bookService.getById(bookEntity.getId().toString());
        assertNotNull(result);
        assertEquals(result.getId(), bookEntity.getId().toString());
        assertEquals(result.getName(), bookEntity.getName());
        assertEquals(result.getPublicationDate(), bookEntity.getPublicationDate());
        assertEquals(result.getNumberOfPages(), bookEntity.getNumberOfPages());
    }

    @Test
    void givenNonExistingBookId_whenGetById_thenThrowBookNotFoundException() {
        String fakeId = UUID.randomUUID().toString();
        assertThrows(BookNotFoundException.class, () -> this.bookService.getById(fakeId));
    }

    @Test
    void givenInvalidBookId_whenGetById_thenThrowInvalidUUIDException() {
        assertThrows(InvalidUUIDException.class, () -> this.bookService.getById("fake"));
    }

    @Test
    void givenNewBookDTO_whenSave_thenCreateBook() {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setName("Service-Book-Name-" + this.bookRepository.count());
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
        bookDTO.setName("Service-Book-Name-" + this.bookRepository.count());
        bookDTO.setPublicationDate(LocalDate.now());
        bookDTO.setNumberOfPages(Integer.valueOf(1));

        this.bookService.save(bookDTO);

        assertThrows(DuplicatedBookException.class, () -> this.bookService.save(bookDTO));
    }

    @Test
    void givenValidBookId_whenDelete_thenDeleteBook() {
        BooksEntity bookEntity = this.bookRepository.findAll().get(0);
        String id = bookEntity.getId().toString();
        this.bookService.delete(id);
        assertFalse(this.bookRepository.findById(UUID.fromString(id)).isPresent());
    }

    @Test
    void givenInvalidBookId_whenDelete_thenThrowInvalidUUIDException() {
        assertThrows(InvalidUUIDException.class, () -> this.bookService.delete("fake"));
    }

    @Test
    void givenValidBookId_whenUpdate_thenUpdateBook() {
        BooksEntity bookEntity = this.bookRepository.findAll().get(0);
        BookDTO bookDTO = new BookDTO();
        bookDTO.setName("Service-Book-Name-" + (this.bookRepository.count() + 1));
        bookDTO.setPublicationDate(LocalDate.now());
        bookDTO.setNumberOfPages(10);
        BookDTO result = this.bookService.update(bookEntity.getId().toString(), bookDTO);
        assertNotNull(result);
        assertEquals(result.getId(), bookEntity.getId().toString());
        assertEquals(result.getName(), bookDTO.getName());
        assertEquals(result.getPublicationDate(), bookDTO.getPublicationDate());
        assertEquals(result.getNumberOfPages(), bookDTO.getNumberOfPages());
    }

    @Test
    void givenNonExistingBookId_whenUpdate_thenThrowBookNotFoundException() {
        String id = UUID.randomUUID().toString();
        BookDTO bookDTO = new BookDTO();
        assertThrows(BookNotFoundException.class, () -> this.bookService.update(id, bookDTO));
    }

    @Test
    void givenExistingBookDTO_whenUpdate_thenThrowDuplicatedBookException() {
        BooksEntity bookEntity = this.bookRepository.findAll().get(0);
        String id = bookEntity.getId().toString();
        BookDTO bookDTO = new BookDTO();
        bookDTO.setName("Service-Book-Name-0");
        bookDTO.setPublicationDate(LocalDate.now());
        bookDTO.setNumberOfPages(Integer.valueOf(1));
        assertThrows(DuplicatedBookException.class, () -> this.bookService.update(id, bookDTO));
    }

    @Test
    void givenInvalidBookId_whenUpdate_thenThrowInvalidUUIDException() {
        assertThrows(InvalidUUIDException.class, () -> this.bookService.update("fake", null));
    }
}

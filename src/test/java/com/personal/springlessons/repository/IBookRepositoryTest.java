package com.personal.springlessons.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.time.LocalDate;
import java.util.Optional;
import com.personal.springlessons.model.entity.BookEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class IBookRepositoryTest {

    @Autowired
    private IBookRepository bookRepository;

    @Test
    void testFindByNameAndPublicationDate() {
        String name = "Test Book";
        LocalDate publicationDate = LocalDate.now();
        BookEntity bookEntity = new BookEntity();
        bookEntity.setName(name);
        bookEntity.setPublicationDate(publicationDate);
        bookEntity.setNumberOfPages(Integer.valueOf(1));
        this.bookRepository.save(bookEntity);
        Optional<BookEntity> result =
                this.bookRepository.findByNameAndPublicationDate(name, publicationDate);
        assertTrue(result.isPresent());
        assertNotNull(result.get().getId());
        assertEquals(name, result.get().getName());
        assertEquals(publicationDate, result.get().getPublicationDate());
    }

    @Test
    void testFindByNameAndPublicationDate_NotFound() {
        String name = "Nonexistent Book";
        LocalDate publicationDate = LocalDate.now();
        Optional<BookEntity> result =
                this.bookRepository.findByNameAndPublicationDate(name, publicationDate);
        assertFalse(result.isPresent());
    }
}

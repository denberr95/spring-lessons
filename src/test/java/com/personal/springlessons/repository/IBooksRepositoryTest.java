package com.personal.springlessons.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.personal.springlessons.model.entity.books.BooksEntity;
import com.personal.springlessons.model.lov.Channel;
import com.personal.springlessons.model.lov.Genre;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class IBooksRepositoryTest {

    @Autowired
    private IBooksRepository bookRepository;

    private static final int TOTAL = 5;

    @BeforeEach
    void init() {
        List<BooksEntity> bookEntities = new ArrayList<>(TOTAL);
        for (int i = 0; i < TOTAL; i++) {
            BooksEntity bookEntity = new BooksEntity();
            bookEntity.setName("Repository-Book-Name-" + i);
            bookEntity.setPublicationDate(LocalDate.now());
            bookEntity.setNumberOfPages(i + 1);
            bookEntity.setChannel(Channel.NA);
            bookEntity.setGenre(Genre.NA);
            bookEntities.add(bookEntity);
        }
        this.bookRepository.saveAllAndFlush(bookEntities);
    }

    @AfterEach
    void tearDown() {
        this.bookRepository.deleteAll();
    }

    @Test
    void givenExistentBook_whenFindByNameAndPublicationDateAndNumberOfPages_thenBookIsRetrieved() {
        String name = "Repository-Book-Name-0";
        LocalDate publicationDate = LocalDate.now();
        Optional<BooksEntity> result = this.bookRepository
                .findByNameAndPublicationDateAndNumberOfPages(name, publicationDate, 1);
        assertTrue(result.isPresent());
        assertNotNull(result.get().getId());
        assertEquals(name, result.get().getName());
        assertEquals(publicationDate, result.get().getPublicationDate());
        assertNotNull(result.get().getCreatedAt());
        assertNull(result.get().getUpdatedAt());
    }

    @Test
    void givenNonExistentBook_whenFindByNameAndPublicationDateAndNumberOfPages_thenNotFound() {
        String name = "Non existent Book";
        LocalDate publicationDate = LocalDate.now();
        Optional<BooksEntity> result = this.bookRepository
                .findByNameAndPublicationDateAndNumberOfPages(name, publicationDate, 0);
        assertFalse(result.isPresent());
    }
}

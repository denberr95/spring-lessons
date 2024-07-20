package com.personal.springlessons.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.UUID;
import com.personal.springlessons.model.dto.BookDTO;
import com.personal.springlessons.model.dto.BookNotFoundResponseDTO;
import com.personal.springlessons.model.dto.DuplicatedBookResponseDTO;
import com.personal.springlessons.model.lov.APICategory;
import com.personal.springlessons.service.BookService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class BookRestControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private BookService bookService;

    @Value("${spring.mvc.servlet.path}")
    private String basePath;

    private static final int TOTAL_BOOKS = 5;

    @BeforeEach
    void init() {
        for (int i = 0; i < TOTAL_BOOKS; i++) {
            BookDTO bookDTO = new BookDTO();
            bookDTO.setName("Book-" + i);
            bookDTO.setPublicationDate(LocalDate.now());
            bookDTO.setNumberOfPages(new SecureRandom().nextInt(10000) + 1);
            this.bookService.save(bookDTO);
        }
    }

    @AfterEach
    void tearDown() {
        this.bookService.getAll().forEach(x -> {
            this.bookService.delete(x.getId());
        });
    }

    @Test
    void givenBooks_whenGetAllBooks_thenBooksAreReturned() {
        String url = String.format("%s/books", this.basePath);
        ResponseEntity<BookDTO[]> response =
                this.testRestTemplate.getForEntity(url, BookDTO[].class);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(TOTAL_BOOKS, response.getBody().length);
        for (int i = 0; i < TOTAL_BOOKS; i++) {
            BookDTO bookDTO = response.getBody()[i];
            assertNotNull(bookDTO.getId());
            assertNotNull(bookDTO.getName());
            assertNotNull(bookDTO.getPublicationDate());
            assertThat(bookDTO.getNumberOfPages()).isPositive();
        }
    }

    @Test
    void givenEmptyBooksCollection_whenGetAllBooks_thenNotContent() {
        String url = String.format("%s/books", this.basePath);
        this.tearDown();
        ResponseEntity<Void> response = this.testRestTemplate.getForEntity(url, Void.class);
        assertNull(response.getBody());
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void givenExistingBookId_whenGetById_thenBookAreReturned() {
        String url = String.format("%s/books/%s", this.basePath,
                this.bookService.getAll().get(0).getId());
        ResponseEntity<BookDTO> response = this.testRestTemplate.getForEntity(url, BookDTO.class);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        BookDTO bookDTO = response.getBody();
        assertNotNull(bookDTO.getId());
        assertNotNull(bookDTO.getName());
        assertNotNull(bookDTO.getPublicationDate());
        assertThat(bookDTO.getNumberOfPages()).isPositive();
    }

    @Test
    void givenNonExistingBookId_whenGetById_thenReturnBookNotFound() {
        String url = String.format("%s/books/%s", this.basePath, UUID.randomUUID().toString());
        ResponseEntity<BookNotFoundResponseDTO> response =
                this.testRestTemplate.getForEntity(url, BookNotFoundResponseDTO.class);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        BookNotFoundResponseDTO bookNotFoundResponseDTO = response.getBody();
        assertEquals(APICategory.BOOKS, bookNotFoundResponseDTO.getCategory());
        assertNotNull(bookNotFoundResponseDTO.getId());
        assertNotNull(bookNotFoundResponseDTO.getTimestamp());
        assertNotNull(bookNotFoundResponseDTO.getMessage());
    }

    @Test
    void givenNewBook_whenSave_thenBookIsCreated() {
        String url = String.format("%s/books", this.basePath);
        BookDTO bookRequest = new BookDTO();
        bookRequest.setName("New Book Controller");
        bookRequest.setNumberOfPages(1);
        bookRequest.setPublicationDate(LocalDate.now());
        ResponseEntity<BookDTO> response =
                this.testRestTemplate.postForEntity(url, bookRequest, BookDTO.class);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        BookDTO bookDTO = response.getBody();
        assertNotNull(bookDTO.getId());
        assertNotNull(bookDTO.getName());
        assertNotNull(bookDTO.getPublicationDate());
        assertThat(bookDTO.getNumberOfPages()).isPositive();
    }

    @Test
    void givenExistingBook_whenSave_thenReturnDuplicatedBook() {
        String url = String.format("%s/books", this.basePath);
        BookDTO bookRequest = new BookDTO();
        bookRequest.setName("Duplicated Book Controller");
        bookRequest.setNumberOfPages(1);
        bookRequest.setPublicationDate(LocalDate.now());

        ResponseEntity<BookDTO> firstResponse =
                this.testRestTemplate.postForEntity(url, bookRequest, BookDTO.class);
        assertNotNull(firstResponse.getBody());
        assertEquals(HttpStatus.CREATED, firstResponse.getStatusCode());
        BookDTO bookDTO = firstResponse.getBody();
        assertNotNull(bookDTO.getId());
        assertNotNull(bookDTO.getName());
        assertNotNull(bookDTO.getPublicationDate());
        assertThat(bookDTO.getNumberOfPages()).isPositive();

        ResponseEntity<DuplicatedBookResponseDTO> secondResponse = this.testRestTemplate
                .postForEntity(url, bookRequest, DuplicatedBookResponseDTO.class);
        assertNotNull(secondResponse.getBody());
        assertEquals(HttpStatus.CONFLICT, secondResponse.getStatusCode());
        DuplicatedBookResponseDTO duplicatedBookResponseDTO = secondResponse.getBody();
        assertEquals(APICategory.BOOKS, duplicatedBookResponseDTO.getCategory());
        assertNotNull(duplicatedBookResponseDTO.getTimestamp());
        assertNotNull(duplicatedBookResponseDTO.getMessage());
        assertNotNull(duplicatedBookResponseDTO.getAdditionalData());
        assertNotNull(duplicatedBookResponseDTO.getAdditionalData().getOrinalId());
    }

    @Test
    void givenExistingBookId_whenDelete_thenBookIsDeleted() {
        String url = String.format("%s/books/%s", this.basePath,
                this.bookService.getAll().get(0).getId());
        ResponseEntity<Void> response =
                this.testRestTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);
        assertNull(response.getBody());
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}

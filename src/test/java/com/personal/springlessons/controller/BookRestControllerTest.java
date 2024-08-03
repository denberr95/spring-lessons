package com.personal.springlessons.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import java.time.LocalDate;
import java.util.UUID;
import com.personal.springlessons.model.dto.BookDTO;
import com.personal.springlessons.model.dto.BookNotFoundResponseDTO;
import com.personal.springlessons.model.dto.DuplicatedBookResponseDTO;
import com.personal.springlessons.model.dto.InvalidUUIDResponseDTO;
import com.personal.springlessons.model.lov.DomainCategory;
import com.personal.springlessons.service.BookService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
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

    private String buildUrl(String path) {
        return String.format("%s%s", this.basePath, path);
    }

    @BeforeEach
    void init() {
        for (int i = 0; i < TOTAL_BOOKS; i++) {
            BookDTO bookDTO = new BookDTO();
            bookDTO.setName("Controller-Book-Name-" + i);
            bookDTO.setPublicationDate(LocalDate.now());
            bookDTO.setNumberOfPages(i + 1);
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
        String url = this.buildUrl("/books");
        ResponseEntity<BookDTO[]> response =
                this.testRestTemplate.exchange(url, HttpMethod.GET, null, BookDTO[].class);
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
        ResponseEntity<Void> response =
                this.testRestTemplate.exchange(url, HttpMethod.GET, null, Void.class);
        assertNull(response.getBody());
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void givenExistingBookId_whenGetById_thenBookAreReturned() {
        String url = String.format("%s/books/%s", this.basePath,
                this.bookService.getAll().get(0).getId());
        ResponseEntity<BookDTO> response =
                this.testRestTemplate.exchange(url, HttpMethod.GET, null, BookDTO.class);
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
        ResponseEntity<BookNotFoundResponseDTO> response = this.testRestTemplate.exchange(url,
                HttpMethod.GET, null, BookNotFoundResponseDTO.class);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        BookNotFoundResponseDTO responseDTO = response.getBody();
        assertEquals(DomainCategory.BOOKS, responseDTO.getCategory());
        assertNotNull(responseDTO.getId());
        assertNotNull(responseDTO.getTimestamp());
        assertNotNull(responseDTO.getMessage());
    }

    @Test
    void givenInvalidBookId_whenGetById_thenReturnInvalidUUID() {
        String fakId = "fakeId";
        String url = this.buildUrl("/books/" + fakId);
        ResponseEntity<InvalidUUIDResponseDTO> response = this.testRestTemplate.exchange(url,
                HttpMethod.GET, null, InvalidUUIDResponseDTO.class);
        InvalidUUIDResponseDTO responseDTO = response.getBody();
        assertNotNull(responseDTO);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(DomainCategory.BOOKS, responseDTO.getCategory());
        assertNotNull(responseDTO.getTimestamp());
        assertNotNull(responseDTO.getMessage());
        assertNotNull(responseDTO.getAdditionalData());
        assertNotNull(responseDTO.getAdditionalData().getInvalidId());
    }

    @Test
    void givenNewBook_whenSave_thenBookIsCreated() {
        String url = String.format("%s/books", this.basePath);
        BookDTO bookRequest = new BookDTO();
        bookRequest.setName("Controller-Book-Name-" + TOTAL_BOOKS);
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
        bookRequest.setName("Controller-Book-Name-Duplicated");
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
        DuplicatedBookResponseDTO responseDTO = secondResponse.getBody();
        assertEquals(DomainCategory.BOOKS, responseDTO.getCategory());
        assertNotNull(responseDTO.getTimestamp());
        assertNotNull(responseDTO.getMessage());
        assertNotNull(responseDTO.getAdditionalData());
        assertNotNull(responseDTO.getAdditionalData().getOrinalId());
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

    @Test
    void givenInvalidBookId_whenDelete_thenReturnInvalidUUID() {
        String fakId = "fakeId";
        String url = this.buildUrl("/books/" + fakId);
        ResponseEntity<InvalidUUIDResponseDTO> response = this.testRestTemplate.exchange(url,
                HttpMethod.DELETE, null, InvalidUUIDResponseDTO.class);
        InvalidUUIDResponseDTO responseDTO = response.getBody();
        assertNotNull(responseDTO);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(DomainCategory.BOOKS, responseDTO.getCategory());
        assertNotNull(responseDTO.getTimestamp());
        assertNotNull(responseDTO.getMessage());
        assertNotNull(responseDTO.getAdditionalData());
        assertNotNull(responseDTO.getAdditionalData().getInvalidId());
    }

    @Test
    void givenExistingBookId_whenUpdate_thenBookIsUpdated() {
        BookDTO bookOld = this.bookService.getAll().get(0);
        String url = this.buildUrl("/books/" + bookOld.getId());
        bookOld.setName("Controller-Book-Name-0");
        bookOld.setNumberOfPages(10);
        bookOld.setPublicationDate(LocalDate.now());
        HttpEntity<BookDTO> httpEntity = new HttpEntity<>(bookOld);
        ResponseEntity<BookDTO> response =
                this.testRestTemplate.exchange(url, HttpMethod.PUT, httpEntity, BookDTO.class);
        BookDTO bookNew = response.getBody();
        assertNotNull(bookNew);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(bookOld, bookNew);
    }

    @Test
    void givenNonExistingBookId_whenUpdate_thenReturnBookNotFound() {
        String id = UUID.randomUUID().toString();
        String url = this.buildUrl("/books/" + id);
        BookDTO body = new BookDTO();
        HttpEntity<BookDTO> httpEntity = new HttpEntity<>(body);
        ResponseEntity<BookNotFoundResponseDTO> response = this.testRestTemplate.exchange(url,
                HttpMethod.PUT, httpEntity, BookNotFoundResponseDTO.class);
        BookNotFoundResponseDTO responseDTO = response.getBody();
        assertNotNull(responseDTO);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(DomainCategory.BOOKS, responseDTO.getCategory());
        assertNotNull(responseDTO.getId());
        assertNotNull(responseDTO.getTimestamp());
        assertNotNull(responseDTO.getMessage());
    }

    @Test
    void givenExistingBook_whenUpdate_thenReturnDuplicatedBook() {
        BookDTO bookOld = this.bookService.getAll().get(0);
        String url = this.buildUrl("/books/" + bookOld.getId());
        HttpEntity<BookDTO> httpEntity = new HttpEntity<>(bookOld);
        ResponseEntity<DuplicatedBookResponseDTO> response = this.testRestTemplate.exchange(url,
                HttpMethod.PUT, httpEntity, DuplicatedBookResponseDTO.class);
        DuplicatedBookResponseDTO responseDTO = response.getBody();
        assertNotNull(responseDTO);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(DomainCategory.BOOKS, responseDTO.getCategory());
        assertNotNull(responseDTO.getTimestamp());
        assertNotNull(responseDTO.getMessage());
        assertNotNull(responseDTO.getAdditionalData());
        assertNotNull(responseDTO.getAdditionalData().getOrinalId());
    }

    @Test
    void givenInvalidBookId_whenUpdate_thenReturnInvalidUUID() {
        BookDTO bookOld = this.bookService.getAll().get(0);
        String fakId = "fakeId";
        String url = this.buildUrl("/books/" + fakId);
        HttpEntity<BookDTO> httpEntity = new HttpEntity<>(bookOld);
        ResponseEntity<InvalidUUIDResponseDTO> response = this.testRestTemplate.exchange(url,
                HttpMethod.PUT, httpEntity, InvalidUUIDResponseDTO.class);
        InvalidUUIDResponseDTO responseDTO = response.getBody();
        assertNotNull(responseDTO);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(DomainCategory.BOOKS, responseDTO.getCategory());
        assertNotNull(responseDTO.getTimestamp());
        assertNotNull(responseDTO.getMessage());
        assertNotNull(responseDTO.getAdditionalData());
        assertNotNull(responseDTO.getAdditionalData().getInvalidId());
    }
}

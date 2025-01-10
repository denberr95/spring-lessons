package com.personal.springlessons.controller.books;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import com.personal.springlessons.model.dto.BookDTO;
import com.personal.springlessons.model.dto.response.BookNotFoundResponseDTO;
import com.personal.springlessons.model.dto.response.DuplicatedBookResponseDTO;
import com.personal.springlessons.model.dto.response.InvalidUUIDResponseDTO;
import com.personal.springlessons.model.lov.Channel;
import com.personal.springlessons.model.lov.DomainCategory;
import com.personal.springlessons.model.lov.Genre;
import com.personal.springlessons.service.BooksService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class BooksRestControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private BooksService bookService;

    @Value("${spring.mvc.servlet.path}")
    private String basePath;

    @Value("${test.access.client-id-no-permission}")
    private String clientIdNoPermission;

    @Value("${test.access.client-secret-no-permission}")
    private String clientSecretNoPermission;

    @Value("${test.access.client-id-full-permission}")
    private String clientIdFullPermission;

    @Value("${test.access.client-secret-full-permission}")
    private String clientSecretFullPermission;

    @Value("${test.access.idp-url}")
    private String idpUrl;

    @Value("${test.access.grant-type}")
    private String grantType;

    private String validToken;

    private String invalidToken;

    private RestClient restClient;

    UUID fakeId = UUID.randomUUID();

    private static final int TOTAL = 5;

    private String buildUrl(String path) {
        return String.format("%s%s", this.basePath, path);
    }

    private String retrieveAccessToken(String clientId, String clientSecret) {
        this.restClient = RestClient.builder().baseUrl(this.idpUrl).build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", this.grantType);
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);

        Map<?, ?> response = this.restClient.post().body(body)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .headers(httpHeaders -> httpHeaders.addAll(headers)).retrieve().body(Map.class);

        Map<String, Object> responseMap = response.entrySet().stream()
                .collect(Collectors.toMap(e -> (String) e.getKey(), e -> (Object) e.getValue()));
        return responseMap.get("access_token").toString();
    }

    private HttpHeaders retrieveHttpHeaders(String token) {
        HttpHeaders result = new HttpHeaders();
        result.add("Authorization", "Bearer " + token);
        result.add("channel", Channel.NA.toString());
        return result;
    }

    @BeforeEach
    void init() {
        for (int i = 0; i < TOTAL; i++) {
            BookDTO bookDTO = new BookDTO();
            bookDTO.setName("Controller-Book-Name-" + i);
            bookDTO.setPublicationDate(LocalDate.now());
            bookDTO.setNumberOfPages(i + 1);
            this.bookService.save(bookDTO, Channel.NA);
        }
        this.validToken = this.retrieveAccessToken(this.clientIdFullPermission,
                this.clientSecretFullPermission);
        this.invalidToken =
                this.retrieveAccessToken(this.clientIdNoPermission, this.clientSecretNoPermission);
    }

    @AfterEach
    void tearDown() {
        this.bookService.getAll().forEach(x -> {
            this.bookService.delete(x.getId());
        });
    }

    @Test
    void givenInvalidAccessToken_whenCallAPI_thenReturnForbidden() {
        ResponseEntity<?> response = null;

        BookDTO bookRequest = new BookDTO();
        bookRequest.setName("Controller-Book-Name-" + TOTAL);
        bookRequest.setNumberOfPages(1);
        bookRequest.setPublicationDate(LocalDate.now());
        bookRequest.setGenre(Genre.NA);

        HttpHeaders httpHeaders = this.retrieveHttpHeaders(this.invalidToken);
        HttpEntity<BookDTO> httpEntity = new HttpEntity<>(bookRequest, httpHeaders);
        String urlBase = this.buildUrl("/books");
        String urlWithResource = this.buildUrl("/books/" + this.fakeId);

        response = this.testRestTemplate.exchange(urlBase, HttpMethod.GET,
                new HttpEntity<>(httpHeaders), Object.class);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());

        response =
                this.testRestTemplate.exchange(urlBase, HttpMethod.POST, httpEntity, Object.class);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());

        response = this.testRestTemplate.exchange(urlWithResource, HttpMethod.DELETE, httpEntity,
                Object.class);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());

        response = this.testRestTemplate.exchange(urlWithResource, HttpMethod.GET,
                new HttpEntity<>(httpHeaders), Object.class);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());

        response = this.testRestTemplate.exchange(urlWithResource, HttpMethod.PUT, httpEntity,
                Object.class);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void givenWithoutAccessToken_whenCallAPI_thenReturnUnauthorized() {
        ResponseEntity<?> response = null;

        BookDTO bookRequest = new BookDTO();
        bookRequest.setName("Controller-Book-Name-" + TOTAL);
        bookRequest.setNumberOfPages(1);
        bookRequest.setPublicationDate(LocalDate.now());
        bookRequest.setGenre(Genre.NA);

        HttpHeaders httpHeaders = this.retrieveHttpHeaders(null);
        HttpEntity<BookDTO> httpEntity = new HttpEntity<>(bookRequest, httpHeaders);
        String urlBase = this.buildUrl("/books");
        String urlWithResource = this.buildUrl("/books/" + this.fakeId);

        response = this.testRestTemplate.exchange(urlBase, HttpMethod.GET, null, Object.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

        response =
                this.testRestTemplate.exchange(urlBase, HttpMethod.POST, httpEntity, Object.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

        response = this.testRestTemplate.exchange(urlWithResource, HttpMethod.DELETE, httpEntity,
                Object.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

        response =
                this.testRestTemplate.exchange(urlWithResource, HttpMethod.GET, null, Object.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

        response = this.testRestTemplate.exchange(urlWithResource, HttpMethod.PUT, httpEntity,
                Object.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void givenBooks_whenGetAll_thenBooksAreReturned() {
        String url = this.buildUrl("/books");

        HttpEntity<HttpHeaders> httpEntity =
                new HttpEntity<>(this.retrieveHttpHeaders(this.validToken));

        ResponseEntity<BookDTO[]> response =
                this.testRestTemplate.exchange(url, HttpMethod.GET, httpEntity, BookDTO[].class);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(TOTAL, response.getBody().length);
        for (int i = 0; i < TOTAL; i++) {
            BookDTO bookDTO = response.getBody()[i];
            assertNotNull(bookDTO.getId());
            assertNotNull(bookDTO.getName());
            assertNotNull(bookDTO.getPublicationDate());
            assertThat(bookDTO.getNumberOfPages()).isPositive();
        }
    }

    @Test
    void givenEmptyCollection_whenGetAll_thenNoContent() {
        String url = this.buildUrl("/books");
        this.tearDown();

        HttpEntity<HttpHeaders> httpEntity =
                new HttpEntity<>(this.retrieveHttpHeaders(this.validToken));
        ResponseEntity<Void> response =
                this.testRestTemplate.exchange(url, HttpMethod.GET, httpEntity, Void.class);
        assertNull(response.getBody());
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void givenExistingId_whenGetById_thenBookAreReturned() {
        String url = String.format("%s/books/%s", this.basePath,
                this.bookService.getAll().get(0).getId());
        HttpEntity<HttpHeaders> httpEntity =
                new HttpEntity<>(this.retrieveHttpHeaders(this.validToken));
        ResponseEntity<BookDTO> response =
                this.testRestTemplate.exchange(url, HttpMethod.GET, httpEntity, BookDTO.class);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        BookDTO bookDTO = response.getBody();
        assertNotNull(bookDTO.getId());
        assertNotNull(bookDTO.getName());
        assertNotNull(bookDTO.getPublicationDate());
        assertThat(bookDTO.getNumberOfPages()).isPositive();
    }

    @Test
    void givenNonExistingId_whenGetById_thenReturnBookNotFound() {
        String url = String.format("%s/books/%s", this.basePath, UUID.randomUUID().toString());
        HttpEntity<HttpHeaders> httpEntity =
                new HttpEntity<>(this.retrieveHttpHeaders(this.validToken));
        ResponseEntity<BookNotFoundResponseDTO> response = this.testRestTemplate.exchange(url,
                HttpMethod.GET, httpEntity, BookNotFoundResponseDTO.class);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        BookNotFoundResponseDTO responseDTO = response.getBody();
        assertEquals(DomainCategory.BOOKS, responseDTO.getCategory());
        assertNotNull(responseDTO.getAdditionalData().getId());
        assertNotNull(responseDTO.getTimestamp());
        assertNotNull(responseDTO.getMessage());
    }

    @Test
    void givenInvalidId_whenGetById_thenReturnInvalidUUID() {
        String url = this.buildUrl("/books/getFakeId");

        HttpEntity<HttpHeaders> httpEntity =
                new HttpEntity<>(this.retrieveHttpHeaders(this.validToken));

        ResponseEntity<InvalidUUIDResponseDTO> response = this.testRestTemplate.exchange(url,
                HttpMethod.GET, httpEntity, InvalidUUIDResponseDTO.class);
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
        String url = this.buildUrl("/books");
        BookDTO bookRequest = new BookDTO();
        bookRequest.setName("Controller-Book-Name-" + TOTAL);
        bookRequest.setNumberOfPages(1);
        bookRequest.setPublicationDate(LocalDate.now());
        bookRequest.setGenre(Genre.NA);

        HttpEntity<BookDTO> httpEntity =
                new HttpEntity<>(bookRequest, this.retrieveHttpHeaders(this.validToken));

        ResponseEntity<BookDTO> response =
                this.testRestTemplate.exchange(url, HttpMethod.POST, httpEntity, BookDTO.class);
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
        String url = this.buildUrl("/books");
        BookDTO bookRequest = new BookDTO();
        bookRequest.setName("Controller-Book-Name-Duplicated");
        bookRequest.setNumberOfPages(1);
        bookRequest.setPublicationDate(LocalDate.now());
        bookRequest.setGenre(Genre.NA);

        HttpEntity<BookDTO> httpEntity =
                new HttpEntity<>(bookRequest, this.retrieveHttpHeaders(this.validToken));

        ResponseEntity<BookDTO> firstResponse =
                this.testRestTemplate.exchange(url, HttpMethod.POST, httpEntity, BookDTO.class);
        assertNotNull(firstResponse.getBody());
        assertEquals(HttpStatus.CREATED, firstResponse.getStatusCode());
        BookDTO bookDTO = firstResponse.getBody();
        assertNotNull(bookDTO.getId());
        assertNotNull(bookDTO.getName());
        assertNotNull(bookDTO.getPublicationDate());
        assertThat(bookDTO.getNumberOfPages()).isPositive();

        ResponseEntity<DuplicatedBookResponseDTO> secondResponse = this.testRestTemplate
                .exchange(url, HttpMethod.POST, httpEntity, DuplicatedBookResponseDTO.class);
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
    void givenExistingId_whenDelete_thenBookIsDeleted() {
        String url = String.format("%s/books/%s", this.basePath,
                this.bookService.getAll().get(0).getId());

        HttpEntity<HttpHeaders> httpEntity =
                new HttpEntity<>(this.retrieveHttpHeaders(this.validToken));
        ResponseEntity<Void> response =
                this.testRestTemplate.exchange(url, HttpMethod.DELETE, httpEntity, Void.class);
        assertNull(response.getBody());
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void givenInvalidId_whenDelete_thenReturnInvalidUUID() {
        String url = this.buildUrl("/books/deleteFakeId");

        HttpEntity<HttpHeaders> httpEntity =
                new HttpEntity<>(this.retrieveHttpHeaders(this.validToken));

        ResponseEntity<InvalidUUIDResponseDTO> response = this.testRestTemplate.exchange(url,
                HttpMethod.DELETE, httpEntity, InvalidUUIDResponseDTO.class);
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
    void givenExistingId_whenUpdate_thenBookIsUpdated() {
        BookDTO bookOld = this.bookService.getAll().get(0);
        String url = this.buildUrl("/books/" + bookOld.getId());
        bookOld.setName("Controller-Book-Name-0");
        bookOld.setNumberOfPages(10);
        bookOld.setPublicationDate(LocalDate.now());
        bookOld.setGenre(Genre.NA);

        HttpEntity<BookDTO> httpEntity =
                new HttpEntity<>(bookOld, this.retrieveHttpHeaders(this.validToken));

        ResponseEntity<BookDTO> response =
                this.testRestTemplate.exchange(url, HttpMethod.PUT, httpEntity, BookDTO.class);
        BookDTO bookNew = response.getBody();
        assertNotNull(bookNew);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(bookOld, bookNew);
    }

    @Test
    void givenNonExistingId_whenUpdate_thenReturnBookNotFound() {
        String url = this.buildUrl("/books/" + this.fakeId);
        BookDTO bookRequest = new BookDTO();
        bookRequest.setName("Controller-Book-Name-" + TOTAL);
        bookRequest.setNumberOfPages(1);
        bookRequest.setPublicationDate(LocalDate.now());
        bookRequest.setGenre(Genre.NA);

        HttpEntity<BookDTO> httpEntity =
                new HttpEntity<>(bookRequest, this.retrieveHttpHeaders(this.validToken));

        ResponseEntity<BookNotFoundResponseDTO> response = this.testRestTemplate.exchange(url,
                HttpMethod.PUT, httpEntity, BookNotFoundResponseDTO.class);
        BookNotFoundResponseDTO responseDTO = response.getBody();
        assertNotNull(responseDTO);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(DomainCategory.BOOKS, responseDTO.getCategory());
        assertNotNull(responseDTO.getAdditionalData().getId());
        assertNotNull(responseDTO.getTimestamp());
        assertNotNull(responseDTO.getMessage());
    }

    @Test
    void givenExistingBook_whenUpdate_thenReturnDuplicatedBook() {
        BookDTO bookOld = this.bookService.getAll().get(0);
        String url = this.buildUrl("/books/" + bookOld.getId());

        HttpEntity<BookDTO> httpEntity =
                new HttpEntity<>(bookOld, this.retrieveHttpHeaders(this.validToken));

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
        String url = this.buildUrl("/books/updateFakeId");

        HttpEntity<BookDTO> httpEntity =
                new HttpEntity<>(bookOld, this.retrieveHttpHeaders(this.validToken));

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

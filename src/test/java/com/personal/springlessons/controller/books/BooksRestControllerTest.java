package com.personal.springlessons.controller.books;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import com.personal.springlessons.model.dto.BookDTO;
import com.personal.springlessons.model.dto.response.BookNotFoundResponseDTO;
import com.personal.springlessons.model.dto.response.DuplicatedBookResponseDTO;
import com.personal.springlessons.model.dto.response.InvalidCSVContentResponseDTO;
import com.personal.springlessons.model.dto.response.InvalidFileTypeResponseDTO;
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
import org.springframework.core.io.Resource;
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

    @Value("classpath:books/valid-file-content.csv")
    private Resource validFileContent;

    @Value("classpath:books/invalid-file-content.csv")
    private Resource invalidFileContent;

    @Value("classpath:books/invalid-file-type.txt")
    private Resource invalidFileType;

    private UUID fakeId = UUID.randomUUID();
    private String downloadUrl;
    private String uploadUrl;
    private String baseUrl;
    private String resourceUrl;
    private String fakeResourceUrl;
    private String validToken;
    private String invalidToken;
    private RestClient restClient;
    private static final int TOTAL = 5;

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
            String name = "Controller-Book-Name-" + i;
            Integer numberOfPages = i + 1;
            LocalDate publicationDate = LocalDate.now();
            BookDTO bookDTO = new BookDTO(null, name, numberOfPages, publicationDate, Genre.NA);
            this.bookService.save(bookDTO, Channel.NA);
        }
        this.validToken = this.retrieveAccessToken(this.clientIdFullPermission,
                this.clientSecretFullPermission);
        this.invalidToken =
                this.retrieveAccessToken(this.clientIdNoPermission, this.clientSecretNoPermission);
        this.downloadUrl = this.basePath + "/books/download";
        this.uploadUrl = this.basePath + "/books/upload";
        this.baseUrl = this.basePath + "/books";
        this.resourceUrl = this.basePath + "/books/%s";
        this.fakeResourceUrl = String.format(this.resourceUrl, this.fakeId);
    }

    @AfterEach
    void tearDown() {
        this.bookService.getAll().forEach(x -> {
            this.bookService.delete(x.id());
        });
    }

    @Test
    void givenInvalidAccessToken_whenCallAPI_thenReturnForbidden() {
        ResponseEntity<?> response = null;

        String name = "Controller-Book-Name-" + TOTAL;
        Integer numberOfPages = 1;
        LocalDate publicationDate = LocalDate.now();
        Genre genre = Genre.NA;

        BookDTO bookRequest = new BookDTO(null, name, numberOfPages, publicationDate, genre);

        HttpHeaders httpHeaders = this.retrieveHttpHeaders(this.invalidToken);
        LinkedMultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();
        parameters.add("file", this.validFileContent);

        response = this.testRestTemplate.exchange(this.baseUrl, HttpMethod.GET,
                new HttpEntity<>(httpHeaders), Object.class);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());

        response = this.testRestTemplate.exchange(this.baseUrl, HttpMethod.POST,
                new HttpEntity<>(bookRequest, httpHeaders), Object.class);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());

        response = this.testRestTemplate.exchange(this.fakeResourceUrl, HttpMethod.DELETE,
                new HttpEntity<>(bookRequest, httpHeaders), Object.class);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());

        response = this.testRestTemplate.exchange(this.fakeResourceUrl, HttpMethod.GET,
                new HttpEntity<>(httpHeaders), Object.class);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());

        response = this.testRestTemplate.exchange(this.fakeResourceUrl, HttpMethod.PUT,
                new HttpEntity<>(bookRequest, httpHeaders), Object.class);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());

        response = this.testRestTemplate.exchange(this.downloadUrl, HttpMethod.GET,
                new HttpEntity<>(httpHeaders), Object.class);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());

        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
        response = this.testRestTemplate.exchange(this.uploadUrl, HttpMethod.POST,
                new HttpEntity<>(parameters, httpHeaders), Object.class);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void givenWithoutAccessToken_whenCallAPI_thenReturnUnauthorized() {
        ResponseEntity<?> response = null;

        String name = "Controller-Book-Name-" + TOTAL;
        Integer numberOfPages = 1;
        LocalDate publicationDate = LocalDate.now();
        Genre genre = Genre.NA;

        BookDTO bookRequest = new BookDTO(null, name, numberOfPages, publicationDate, genre);

        HttpHeaders httpHeaders = this.retrieveHttpHeaders(null);
        HttpEntity<BookDTO> httpEntity = new HttpEntity<>(bookRequest, httpHeaders);

        response = this.testRestTemplate.exchange(this.baseUrl, HttpMethod.GET, null, Object.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

        response = this.testRestTemplate.exchange(this.baseUrl, HttpMethod.POST, httpEntity,
                Object.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

        response = this.testRestTemplate.exchange(this.fakeResourceUrl, HttpMethod.DELETE,
                httpEntity, Object.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

        response = this.testRestTemplate.exchange(this.fakeResourceUrl, HttpMethod.GET, null,
                Object.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

        response = this.testRestTemplate.exchange(this.fakeResourceUrl, HttpMethod.PUT, httpEntity,
                Object.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

        response = this.testRestTemplate.exchange(this.downloadUrl, HttpMethod.GET,
                new HttpEntity<>(httpHeaders), Object.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

        response = this.testRestTemplate.exchange(this.uploadUrl, HttpMethod.POST,
                new HttpEntity<>(httpHeaders), Object.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void givenBooks_whenGetAll_thenBooksAreReturned() {
        HttpEntity<HttpHeaders> httpEntity =
                new HttpEntity<>(this.retrieveHttpHeaders(this.validToken));

        ResponseEntity<BookDTO[]> response = this.testRestTemplate.exchange(this.baseUrl,
                HttpMethod.GET, httpEntity, BookDTO[].class);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(TOTAL, response.getBody().length);

        for (int i = 0; i < TOTAL; i++) {
            BookDTO bookDTO = response.getBody()[i];
            assertNotNull(bookDTO.id());
            assertNotNull(bookDTO.name());
            assertNotNull(bookDTO.publicationDate());
            assertThat(bookDTO.numberOfPages()).isPositive();
        }
    }


    @Test
    void givenEmptyCollection_whenGetAll_thenNoContent() {
        this.tearDown();
        HttpEntity<HttpHeaders> httpEntity =
                new HttpEntity<>(this.retrieveHttpHeaders(this.validToken));
        ResponseEntity<Void> response = this.testRestTemplate.exchange(this.baseUrl, HttpMethod.GET,
                httpEntity, Void.class);
        assertNull(response.getBody());
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void givenExistingId_whenGetById_thenBookAreReturned() {
        String url = String.format(this.resourceUrl, this.bookService.getAll().get(0).id());
        HttpEntity<HttpHeaders> httpEntity =
                new HttpEntity<>(this.retrieveHttpHeaders(this.validToken));

        ResponseEntity<BookDTO> response =
                this.testRestTemplate.exchange(url, HttpMethod.GET, httpEntity, BookDTO.class);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());

        BookDTO bookDTO = response.getBody();
        assertNotNull(bookDTO.id());
        assertNotNull(bookDTO.name());
        assertNotNull(bookDTO.publicationDate());
        assertThat(bookDTO.numberOfPages()).isPositive();
    }


    @Test
    void givenNonExistingId_whenGetById_thenReturnBookNotFound() {
        String url = String.format(this.resourceUrl, UUID.randomUUID().toString());
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
        String url = String.format(this.resourceUrl, "getFakeId");
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
        BookDTO bookRequest =
                new BookDTO(null, "Controller-Book-Name-" + TOTAL, 1, LocalDate.now(), Genre.NA);

        HttpEntity<BookDTO> httpEntity =
                new HttpEntity<>(bookRequest, this.retrieveHttpHeaders(this.validToken));

        ResponseEntity<BookDTO> response = this.testRestTemplate.exchange(this.baseUrl,
                HttpMethod.POST, httpEntity, BookDTO.class);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        BookDTO bookDTO = response.getBody();
        assertNotNull(bookDTO.id());
        assertNotNull(bookDTO.name());
        assertNotNull(bookDTO.publicationDate());
        assertThat(bookDTO.numberOfPages()).isPositive();
    }


    @Test
    void givenExistingBook_whenSave_thenReturnDuplicatedBook() {
        BookDTO bookRequest =
                new BookDTO(null, "Controller-Book-Name-Duplicated", 1, LocalDate.now(), Genre.NA);

        HttpEntity<BookDTO> httpEntity =
                new HttpEntity<>(bookRequest, this.retrieveHttpHeaders(this.validToken));

        ResponseEntity<BookDTO> firstResponse = this.testRestTemplate.exchange(this.baseUrl,
                HttpMethod.POST, httpEntity, BookDTO.class);
        assertNotNull(firstResponse.getBody());
        assertEquals(HttpStatus.CREATED, firstResponse.getStatusCode());

        BookDTO bookDTO = firstResponse.getBody();
        assertNotNull(bookDTO.id());
        assertNotNull(bookDTO.name());
        assertNotNull(bookDTO.publicationDate());
        assertThat(bookDTO.numberOfPages()).isPositive();

        ResponseEntity<DuplicatedBookResponseDTO> secondResponse = this.testRestTemplate.exchange(
                this.baseUrl, HttpMethod.POST, httpEntity, DuplicatedBookResponseDTO.class);
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
        String url = String.format(this.resourceUrl, this.bookService.getAll().get(0).id());
        HttpEntity<HttpHeaders> httpEntity =
                new HttpEntity<>(this.retrieveHttpHeaders(this.validToken));
        ResponseEntity<Void> response =
                this.testRestTemplate.exchange(url, HttpMethod.DELETE, httpEntity, Void.class);
        assertNull(response.getBody());
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void givenInvalidId_whenDelete_thenReturnInvalidUUID() {
        String url = String.format(this.resourceUrl, "deleteFakeId");
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
        String url = String.format(this.resourceUrl, bookOld.id());
        BookDTO bookRequest = new BookDTO(null, bookOld.name(), 10, LocalDate.now(), Genre.NA);
        HttpEntity<BookDTO> httpEntity =
                new HttpEntity<>(bookRequest, this.retrieveHttpHeaders(this.validToken));
        ResponseEntity<BookDTO> response =
                this.testRestTemplate.exchange(url, HttpMethod.PUT, httpEntity, BookDTO.class);
        BookDTO bookNew = response.getBody();
        assertNotNull(bookNew);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(bookRequest.name(), bookNew.name());
        assertEquals(bookRequest.numberOfPages(), bookNew.numberOfPages());
        assertEquals(bookRequest.publicationDate(), bookNew.publicationDate());
        assertEquals(bookOld.genre(), bookNew.genre());
    }

    @Test
    void givenNonExistingId_whenUpdate_thenReturnBookNotFound() {
        BookDTO bookRequest =
                new BookDTO(null, "Controller-Book-Name-" + TOTAL, 1, LocalDate.now(), Genre.NA);
        HttpEntity<BookDTO> httpEntity =
                new HttpEntity<>(bookRequest, this.retrieveHttpHeaders(this.validToken));
        ResponseEntity<BookNotFoundResponseDTO> response = this.testRestTemplate.exchange(
                this.fakeResourceUrl, HttpMethod.PUT, httpEntity, BookNotFoundResponseDTO.class);
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
        String url = String.format(this.resourceUrl, bookOld.id());
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
        String url = String.format(this.resourceUrl, "updateFakeId");
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

    @Test
    void whenDownloadFile_thenReturnCSV() {
        HttpEntity<BookDTO> httpEntity =
                new HttpEntity<>(this.retrieveHttpHeaders(this.validToken));
        ResponseEntity<Resource> response = this.testRestTemplate.exchange(this.downloadUrl,
                HttpMethod.GET, httpEntity, Resource.class);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getHeaders().getContentDisposition().isAttachment());
    }

    @Test
    void givenUploadCsv_whenUpload_thenNoContent() {
        HttpHeaders httpHeaders = this.retrieveHttpHeaders(this.validToken);
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);

        LinkedMultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();
        parameters.add("file", this.validFileContent);

        ResponseEntity<Void> response = this.testRestTemplate.exchange(this.uploadUrl,
                HttpMethod.POST, new HttpEntity<>(parameters, httpHeaders), Void.class);
        assertNull(response.getBody());
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void givenInvalidFileType_whenUpload_thenReturnInvalidFileType() {
        HttpHeaders httpHeaders = this.retrieveHttpHeaders(this.validToken);
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);

        LinkedMultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();
        parameters.add("file", this.invalidFileType);

        HttpEntity<LinkedMultiValueMap<String, Object>> httpEntity =
                new HttpEntity<>(parameters, httpHeaders);

        ResponseEntity<InvalidFileTypeResponseDTO> response = this.testRestTemplate.exchange(
                this.uploadUrl, HttpMethod.POST, httpEntity, InvalidFileTypeResponseDTO.class);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void givenCsvContentMalformed_whenUpload_thenReturnInvalidCSVContent() {
        HttpHeaders httpHeaders = this.retrieveHttpHeaders(this.validToken);
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);

        LinkedMultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();
        parameters.add("file", this.invalidFileContent);

        HttpEntity<LinkedMultiValueMap<String, Object>> httpEntity =
                new HttpEntity<>(parameters, httpHeaders);

        ResponseEntity<InvalidCSVContentResponseDTO> response = this.testRestTemplate.exchange(
                this.uploadUrl, HttpMethod.POST, httpEntity, InvalidCSVContentResponseDTO.class);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}

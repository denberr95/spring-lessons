package com.personal.springlessons.controller.books;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.personal.springlessons.model.dto.BookDTO;
import com.personal.springlessons.model.dto.wrapper.BooksWrapperDTO;
import com.personal.springlessons.model.lov.Channel;
import com.personal.springlessons.model.lov.Genre;
import com.personal.springlessons.service.books.BooksService;
import com.personal.springlessons.util.Methods;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@AutoConfigureRestTestClient
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class BooksRestControllerTest {

  @Autowired
  private RestTestClient restTestClient;

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

    Map<?, ?> response =
        this.restClient.post().body(body).contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .headers(httpHeaders -> httpHeaders.addAll(headers)).retrieve().body(Map.class);

    Map<String, Object> responseMap = response.entrySet().stream()
        .collect(Collectors.toMap(e -> (String) e.getKey(), e -> (Object) e.getValue()));
    return responseMap.get("access_token").toString();
  }

  private Consumer<HttpHeaders> retrieveHttpHeaders(String token) {
    return headers -> {
      if (token != null) {
        headers.setBearerAuth(token);
      }
      headers.add("channel", Channel.NA.toString());
    };
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
    this.validToken =
        this.retrieveAccessToken(this.clientIdFullPermission, this.clientSecretFullPermission);
    this.invalidToken =
        this.retrieveAccessToken(this.clientIdNoPermission, this.clientSecretNoPermission);
    this.downloadUrl = this.basePath + "/v1/books/download";
    this.uploadUrl = this.basePath + "/v1/books/upload";
    this.baseUrl = this.basePath + "/v1/books";
    this.resourceUrl = this.basePath + "/v1/books/%s";
    this.fakeResourceUrl = String.format(this.resourceUrl, this.fakeId);
  }

  @AfterEach
  void tearDown() {
    this.bookService.getAll().getBookDTOs().forEach(x -> {
      BooksWrapperDTO wrapper = this.bookService.getById(x.id());
      String version = Methods.getEtag(wrapper.getHttpHeaders());
      this.bookService.delete(x.id(), version);
    });
  }

  @Test
  void givenInvalidAccessToken_whenCallAPI_thenReturnForbidden() {
    this.restTestClient.get().uri(this.baseUrl).headers(this.retrieveHttpHeaders(this.invalidToken))
        .exchange().expectStatus().isForbidden();
  }

  @Test
  void givenWithoutAccessToken_whenCallAPI_thenReturnUnauthorized() {
    this.restTestClient.get().uri(this.baseUrl).headers(this.retrieveHttpHeaders(null)).exchange()
        .expectStatus().isUnauthorized();
  }

  @Test
  void givenBooks_whenGetAll_thenBooksAreReturned() {
    this.restTestClient.get().uri(this.baseUrl).headers(this.retrieveHttpHeaders(this.validToken))
        .exchange().expectStatus().isOk();
  }

  @Test
  void givenEmptyCollection_whenGetAll_thenNoContent() {
    this.bookService.getAll().getBookDTOs().forEach(book -> {
      BooksWrapperDTO wrapper = this.bookService.getById(book.id());
      String version = Methods.getEtag(wrapper.getHttpHeaders());
      this.bookService.delete(book.id(), version);
    });

    this.restTestClient.get().uri(this.baseUrl).headers(this.retrieveHttpHeaders(this.validToken))
        .exchange().expectStatus().isNoContent();
  }

  @Test
  void givenExistingId_whenGetById_thenBookAreReturned() {
    String id = this.bookService.getAll().getBookDTOs().get(0).id();

    this.restTestClient.get().uri(String.format(this.resourceUrl, id))
        .headers(this.retrieveHttpHeaders(this.validToken)).exchange().expectStatus().isOk();
  }

  @Test
  void givenNonExistingId_whenGetById_thenReturnBookNotFound() {
    this.restTestClient.get().uri(this.fakeResourceUrl)
        .headers(this.retrieveHttpHeaders(this.validToken)).exchange().expectStatus().isNotFound();
  }

  @Test
  void givenInvalidId_whenGetById_thenReturnInvalidUUID() {
    this.restTestClient.get().uri(String.format(this.resourceUrl, "not-a-uuid"))
        .headers(this.retrieveHttpHeaders(this.validToken)).exchange().expectStatus()
        .isBadRequest();
  }

  @Test
  void givenNewBook_whenSave_thenBookIsCreated() {
    BookDTO newBook =
        new BookDTO(null, "Brand New Book", 300, LocalDate.of(2024, 6, 1), Genre.MYSTERY);

    this.restTestClient.post().uri(this.baseUrl).contentType(MediaType.APPLICATION_JSON)
        .headers(this.retrieveHttpHeaders(this.validToken)).body(newBook).exchange().expectStatus()
        .isCreated();
  }

  @Test
  void givenExistingBook_whenSave_thenReturnDuplicatedBook() {
    List<BookDTO> books = this.bookService.getAll().getBookDTOs();
    BookDTO existing = books.get(0);
    BookDTO dup = new BookDTO(null, existing.name(), existing.numberOfPages(),
        existing.publicationDate(), existing.genre());

    this.restTestClient.post().uri(this.baseUrl).contentType(MediaType.APPLICATION_JSON)
        .headers(this.retrieveHttpHeaders(this.validToken)).body(dup).exchange().expectStatus()
        .isEqualTo(409);
  }

  @Test
  void givenExistingId_whenDelete_thenBookIsDeleted() {
    String id = this.bookService.getAll().getBookDTOs().get(0).id();
    String etag = this.bookService.getById(id).getHttpHeaders().getFirst("ETag");

    this.restTestClient.delete().uri(String.format(this.resourceUrl, id)).headers(headers -> {
      this.retrieveHttpHeaders(this.validToken).accept(headers);
      headers.add(HttpHeaders.IF_MATCH, etag);
    }).exchange().expectStatus().isNoContent();
  }

  @Test
  void givenInvalidId_whenDelete_thenReturnInvalidUUID() {
    this.restTestClient.delete().uri(String.format(this.resourceUrl, "not-a-uuid"))
        .headers(headers -> {
          this.retrieveHttpHeaders(this.validToken).accept(headers);
          headers.add(HttpHeaders.IF_MATCH, "\"0\"");
        }).exchange().expectStatus().isBadRequest();
  }

  @Test
  void givenExistingId_whenUpdate_thenBookIsUpdated() {
    String id = this.bookService.getAll().getBookDTOs().get(0).id();
    String etag = this.bookService.getById(id).getHttpHeaders().getFirst("ETag");
    BookDTO updated =
        new BookDTO(null, "Updated Controller Book", 200, LocalDate.of(2025, 1, 1), Genre.FANTASY);

    this.restTestClient.put().uri(String.format(this.resourceUrl, id))
        .contentType(MediaType.APPLICATION_JSON).headers(headers -> {
          this.retrieveHttpHeaders(this.validToken).accept(headers);
          headers.add(HttpHeaders.IF_MATCH, etag);
        }).body(updated).exchange().expectStatus().isOk();
  }

  @Test
  void givenNonExistingId_whenUpdate_thenReturnBookNotFound() {
    BookDTO dto = new BookDTO(null, "Some Book", 100, LocalDate.of(2024, 1, 1), Genre.NOIR);

    this.restTestClient.put().uri(this.fakeResourceUrl).contentType(MediaType.APPLICATION_JSON)
        .headers(headers -> {
          this.retrieveHttpHeaders(this.validToken).accept(headers);
          headers.add(HttpHeaders.IF_MATCH, "\"0\"");
        }).body(dto).exchange().expectStatus().isNotFound();
  }

  @Test
  void givenExistingBook_whenUpdate_thenReturnDuplicatedBook() {
    List<BookDTO> books = this.bookService.getAll().getBookDTOs();
    BookDTO book0 = books.get(0);
    BookDTO book1 = books.get(1);
    String id1 = book1.id();
    String etag1 = this.bookService.getById(id1).getHttpHeaders().getFirst("ETag");
    BookDTO duplicateData = new BookDTO(null, book0.name(), book0.numberOfPages(),
        book0.publicationDate(), book0.genre());

    this.restTestClient.put().uri(String.format(this.resourceUrl, id1))
        .contentType(MediaType.APPLICATION_JSON).headers(headers -> {
          this.retrieveHttpHeaders(this.validToken).accept(headers);
          headers.add(HttpHeaders.IF_MATCH, etag1);
        }).body(duplicateData).exchange().expectStatus().isEqualTo(409);
  }

  @Test
  void givenInvalidBookId_whenUpdate_thenReturnInvalidUUID() {
    BookDTO dto = new BookDTO(null, "Some Book", 100, LocalDate.of(2024, 1, 1), Genre.NOIR);

    this.restTestClient.put().uri(String.format(this.resourceUrl, "not-a-uuid"))
        .contentType(MediaType.APPLICATION_JSON).headers(headers -> {
          this.retrieveHttpHeaders(this.validToken).accept(headers);
          headers.add(HttpHeaders.IF_MATCH, "\"0\"");
        }).body(dto).exchange().expectStatus().isBadRequest();
  }

  @Test
  void whenDownloadFile_thenReturnCSV() {
    this.restTestClient.get().uri(this.downloadUrl)
        .headers(this.retrieveHttpHeaders(this.validToken)).exchange().expectStatus().isOk();
  }

  @Test
  void givenUploadCsv_whenUpload_thenNoContent() {
    org.springframework.http.client.MultipartBodyBuilder builder =
        new org.springframework.http.client.MultipartBodyBuilder();
    builder.part("file", this.validFileContent);

    this.restTestClient.post().uri(this.uploadUrl)
        .headers(this.retrieveHttpHeaders(this.validToken)).body(builder.build()).exchange()
        .expectStatus().isNoContent();
  }

  @Test
  void givenInvalidFileType_whenUpload_thenReturnInvalidFileType() {
    org.springframework.http.client.MultipartBodyBuilder builder =
        new org.springframework.http.client.MultipartBodyBuilder();
    builder.part("file", this.invalidFileType);

    this.restTestClient.post().uri(this.uploadUrl)
        .headers(this.retrieveHttpHeaders(this.validToken)).body(builder.build()).exchange()
        .expectStatus().isBadRequest();
  }

  @Test
  void givenCsvContentMalformed_whenUpload_thenReturnInvalidCSVContent() {
    org.springframework.http.client.MultipartBodyBuilder builder =
        new org.springframework.http.client.MultipartBodyBuilder();
    builder.part("file", this.invalidFileContent);

    this.restTestClient.post().uri(this.uploadUrl)
        .headers(this.retrieveHttpHeaders(this.validToken)).body(builder.build()).exchange()
        .expectStatus().isBadRequest();
  }
}

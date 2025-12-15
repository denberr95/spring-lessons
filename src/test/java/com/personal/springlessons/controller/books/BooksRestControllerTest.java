package com.personal.springlessons.controller.books;

import java.time.LocalDate;
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
    // TODO
  }

  @Test
  void givenWithoutAccessToken_whenCallAPI_thenReturnUnauthorized() {
    // TODO
  }

  @Test
  void givenBooks_whenGetAll_thenBooksAreReturned() {
    // TODO
  }

  @Test
  void givenEmptyCollection_whenGetAll_thenNoContent() {
    // TODO
  }

  @Test
  void givenExistingId_whenGetById_thenBookAreReturned() {
    // TODO
  }

  @Test
  void givenNonExistingId_whenGetById_thenReturnBookNotFound() {
    // TODO
  }

  @Test
  void givenInvalidId_whenGetById_thenReturnInvalidUUID() {
    // TODO
  }

  @Test
  void givenNewBook_whenSave_thenBookIsCreated() {
    // TODO
  }

  @Test
  void givenExistingBook_whenSave_thenReturnDuplicatedBook() {
    // TODO
  }

  @Test
  void givenExistingId_whenDelete_thenBookIsDeleted() {
    // TODO
  }

  @Test
  void givenInvalidId_whenDelete_thenReturnInvalidUUID() {
    // TODO
  }

  @Test
  void givenExistingId_whenUpdate_thenBookIsUpdated() {
    // TODO
  }

  @Test
  void givenNonExistingId_whenUpdate_thenReturnBookNotFound() {
    // TODO
  }

  @Test
  void givenExistingBook_whenUpdate_thenReturnDuplicatedBook() {
    // TODO
  }

  @Test
  void givenInvalidBookId_whenUpdate_thenReturnInvalidUUID() {
    // TODO
  }

  @Test
  void whenDownloadFile_thenReturnCSV() {
    // TODO
  }

  @Test
  void givenUploadCsv_whenUpload_thenNoContent() {
    // TODO
  }

  @Test
  void givenInvalidFileType_whenUpload_thenReturnInvalidFileType() {
    // TODO
  }

  @Test
  void givenCsvContentMalformed_whenUpload_thenReturnInvalidCSVContent() {
    // TODO
  }
}

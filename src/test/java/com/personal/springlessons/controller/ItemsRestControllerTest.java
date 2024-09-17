package com.personal.springlessons.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.personal.springlessons.model.dto.ItemDTO;
import com.personal.springlessons.service.ItemsService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
class ItemsRestControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private ItemsService itemsService;

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

    private static final int TOTAL = 5;

    private List<ItemDTO> data = new ArrayList<>(TOTAL);

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
        return result;
    }

    @BeforeEach
    void init() {
        for (int i = 0; i < TOTAL; i++) {
            ItemDTO itemDTO = new ItemDTO();
            itemDTO.setName("Controller-Item-Name-" + i);
            itemDTO.setBarcode("Controller-Barcode-" + i);
            itemDTO.setPrice(new BigDecimal(9_999));
            this.data.add(itemDTO);
        }
        this.validToken = this.retrieveAccessToken(this.clientIdFullPermission,
                this.clientSecretFullPermission);
        this.invalidToken =
                this.retrieveAccessToken(this.clientIdNoPermission, this.clientSecretNoPermission);
    }

    @AfterEach
    void tearDown() {
        this.data.clear();
    }

    @Test
    void givenInvalidAccessToken_whenCallAPI_thenReturnForbidden() {
        ResponseEntity<?> response = null;
        HttpHeaders httpHeaders = this.retrieveHttpHeaders(this.invalidToken);
        HttpEntity<List<ItemDTO>> httpEntity = new HttpEntity<>(this.data, httpHeaders);
        String urlBase = this.buildUrl("/items");

        response = this.testRestTemplate.exchange(urlBase, HttpMethod.GET,
                new HttpEntity<>(httpHeaders), Object.class);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());

        response = this.testRestTemplate.exchange(urlBase, HttpMethod.POST, httpEntity, Void.class);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());

        response =
                this.testRestTemplate.exchange(urlBase, HttpMethod.DELETE, httpEntity, Void.class);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void givenWithoutAccessToken_whenCallAPI_thenReturnUnauthorized() {
        ResponseEntity<?> response = null;
        HttpEntity<List<ItemDTO>> httpEntity = new HttpEntity<>(this.data);
        String urlBase = this.buildUrl("/items");

        response = this.testRestTemplate.exchange(urlBase, HttpMethod.GET, null, Object.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

        response = this.testRestTemplate.exchange(urlBase, HttpMethod.POST, httpEntity, Void.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

        response =
                this.testRestTemplate.exchange(urlBase, HttpMethod.DELETE, httpEntity, Void.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void givenEmptyCollection_whenGetAll_thenItemsAreRetrieved() {
        String url = this.buildUrl("/items");
        HttpEntity<HttpHeaders> httpEntity =
                new HttpEntity<>(this.retrieveHttpHeaders(this.validToken));

        ResponseEntity<ItemDTO[]> response =
                this.testRestTemplate.exchange(url, HttpMethod.GET, httpEntity, ItemDTO[].class);
        assertNull(response.getBody());
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    @Disabled("Test not completed to implement")
    void givenEItems_whenGetAll_thenItemsAreRetrieved() {
        String url = this.buildUrl("/items");
        HttpEntity<HttpHeaders> httpEntity =
                new HttpEntity<>(this.retrieveHttpHeaders(this.validToken));
                
        ResponseEntity<ItemDTO[]> response =
                this.testRestTemplate.exchange(url, HttpMethod.GET, httpEntity, ItemDTO[].class);
        assertNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        for (int i = 0; i < TOTAL; i++) {
            ItemDTO itemDTO = response.getBody()[i];
            assertNotNull(itemDTO.getId());
            assertNotNull(itemDTO.getName());
            assertNotNull(itemDTO.getBarcode());
            assertNotNull(itemDTO.getPrice());
            assertThat(itemDTO.getPrice()).isPositive();
        }
    }
}

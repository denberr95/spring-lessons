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
import com.personal.springlessons.model.entity.ItemsEntity;
import com.personal.springlessons.model.entity.OrderItemsEntity;
import com.personal.springlessons.model.lov.ItemStatus;
import com.personal.springlessons.repository.IItemsRepository;
import com.personal.springlessons.repository.IOrderItemsRepository;
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
class ItemsRestControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private IOrderItemsRepository orderItemsRepository;

    @Autowired
    private IItemsRepository itemsRepository;

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

    private void cleanupData() {
        this.data.clear();
    }

    private void cleanupItems() {
        this.itemsRepository.deleteAll();
    }

    private void cleanupOrders() {
        this.orderItemsRepository.deleteAll();
    }

    @BeforeEach
    void init() {
        OrderItemsEntity orderItemsEntity = new OrderItemsEntity();
        orderItemsEntity.setQuantity(TOTAL);
        orderItemsEntity.setStatus(ItemStatus.NA);
        orderItemsEntity = this.orderItemsRepository.saveAndFlush(orderItemsEntity);

        for (int i = 0; i < TOTAL; i++) {
            String name = "Controller-Item-Name-" + i;
            String barcode = "Controller-Barcode-" + i;
            BigDecimal price = new BigDecimal("9999.99");

            ItemDTO itemDTO = new ItemDTO();
            itemDTO.setName(name);
            itemDTO.setBarcode(barcode);
            itemDTO.setPrice(price);
            this.data.add(itemDTO);

            ItemsEntity itemsEntity = new ItemsEntity();
            itemsEntity.setName(name);
            itemsEntity.setBarcode(barcode);
            itemsEntity.setPrice(price);
            itemsEntity.setItemsStatusEntity(orderItemsEntity);
            this.itemsRepository.saveAndFlush(itemsEntity);

        }
        this.validToken = this.retrieveAccessToken(this.clientIdFullPermission,
                this.clientSecretFullPermission);
        this.invalidToken =
                this.retrieveAccessToken(this.clientIdNoPermission, this.clientSecretNoPermission);
    }

    @AfterEach
    void tearDown() {
        this.cleanupData();
        this.cleanupItems();
        this.cleanupOrders();
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
    void givenItems_whenUpload_thenNoContent() {
        this.cleanupItems();
        this.cleanupOrders();
        String url = this.buildUrl("/items");
        HttpHeaders httpHeaders = this.retrieveHttpHeaders(this.validToken);
        HttpEntity<List<ItemDTO>> httpEntity = new HttpEntity<>(this.data, httpHeaders);

        ResponseEntity<Void> response =
                this.testRestTemplate.exchange(url, HttpMethod.POST, httpEntity, Void.class);
        assertNull(response.getBody());
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals(1, this.orderItemsRepository.count());
        assertEquals(ItemStatus.UPLOAD, this.orderItemsRepository.findAll().get(0).getStatus());
        assertEquals(TOTAL, this.orderItemsRepository.findAll().get(0).getQuantity());
    }

    @Test
    void givenItems_whenDelete_thenNoContent() {
        this.cleanupItems();
        this.cleanupOrders();
        String url = this.buildUrl("/items");
        HttpHeaders httpHeaders = this.retrieveHttpHeaders(this.validToken);
        HttpEntity<List<ItemDTO>> httpEntity = new HttpEntity<>(this.data, httpHeaders);

        ResponseEntity<Void> response =
                this.testRestTemplate.exchange(url, HttpMethod.DELETE, httpEntity, Void.class);
        assertNull(response.getBody());
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals(1, this.orderItemsRepository.count());
        assertEquals(ItemStatus.DELETE, this.orderItemsRepository.findAll().get(0).getStatus());
        assertEquals(TOTAL, this.orderItemsRepository.findAll().get(0).getQuantity());
    }

    @Test
    void givenEmptyCollection_whenGetAll_thenNoContent() {
        this.tearDown();
        String url = this.buildUrl("/items");
        HttpEntity<HttpHeaders> httpEntity =
                new HttpEntity<>(this.retrieveHttpHeaders(this.validToken));

        ResponseEntity<Void> response =
                this.testRestTemplate.exchange(url, HttpMethod.GET, httpEntity, Void.class);
        assertNull(response.getBody());
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void givenPartialEmptyCollection_whenGetAll_thenNoContent() {
        this.cleanupItems();
        String url = this.buildUrl("/items");
        HttpEntity<HttpHeaders> httpEntity =
                new HttpEntity<>(this.retrieveHttpHeaders(this.validToken));
        ResponseEntity<Void> responseWithoutData =
                this.testRestTemplate.exchange(url, HttpMethod.GET, httpEntity, Void.class);
        assertNull(responseWithoutData.getBody());
        assertEquals(HttpStatus.NO_CONTENT, responseWithoutData.getStatusCode());
    }

    @Test
    void givenItems_whenGetAll_thenItemsRetrieved() {
        String url = this.buildUrl("/items");
        HttpEntity<HttpHeaders> httpEntity =
                new HttpEntity<>(this.retrieveHttpHeaders(this.validToken));

        ResponseEntity<ItemDTO[]> response =
                this.testRestTemplate.exchange(url, HttpMethod.GET, httpEntity, ItemDTO[].class);
        assertNotNull(response.getBody());
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

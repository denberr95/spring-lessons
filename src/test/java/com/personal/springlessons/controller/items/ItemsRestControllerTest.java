package com.personal.springlessons.controller.items;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.personal.springlessons.model.dto.ItemDTO;
import com.personal.springlessons.model.entity.items.ItemsEntity;
import com.personal.springlessons.model.entity.items.OrderItemsEntity;
import com.personal.springlessons.model.lov.Channel;
import com.personal.springlessons.repository.items.IItemsRepository;
import com.personal.springlessons.repository.items.IOrderItemsRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@AutoConfigureRestTestClient
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ItemsRestControllerTest {

  @Autowired
  private RestTestClient restTestClient;

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

  private void cleanupData() {
    this.data.clear();
  }

  private void cleanupItems() {
    this.itemsRepository.deleteAllInBatch();
    this.itemsRepository.flush();
  }

  private void cleanupOrders() {
    this.orderItemsRepository.deleteAllInBatch();
    this.orderItemsRepository.flush();
  }

  @BeforeEach
  void init() {
    OrderItemsEntity orderItemsEntity = new OrderItemsEntity();
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
      itemsEntity.setOrderItemsEntity(orderItemsEntity);
      this.itemsRepository.saveAndFlush(itemsEntity);
    }

    this.validToken =
        this.retrieveAccessToken(this.clientIdFullPermission, this.clientSecretFullPermission);
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
    this.restTestClient.get().uri(this.buildUrl("/v1/items"))
        .headers(this.retrieveHttpHeaders(this.invalidToken)).exchange().expectStatus()
        .isForbidden();
  }

  @Test
  void givenWithoutAccessToken_whenCallAPI_thenReturnUnauthorized() {
    this.restTestClient.get().uri(this.buildUrl("/v1/items"))
        .headers(this.retrieveHttpHeaders(null)).exchange().expectStatus().isUnauthorized();
  }

  @Test
  void givenItems_whenUpload_thenNoContent() {
    ItemDTO item = new ItemDTO();
    item.setName("Upload Item");
    item.setBarcode("CTRL-UPL001");
    item.setPrice(new BigDecimal("9.99"));

    com.personal.springlessons.model.dto.OrderItemsDTO order =
        new com.personal.springlessons.model.dto.OrderItemsDTO();
    order.setItems(List.of(item));

    this.restTestClient.post().uri(this.buildUrl("/v1/items"))
        .contentType(MediaType.APPLICATION_JSON).headers(this.retrieveHttpHeaders(this.validToken))
        .body(order).exchange().expectStatus().isOk();
  }

  @Test
  void givenItems_whenDelete_thenNoContent() {
    OrderItemsEntity orderEntity = this.orderItemsRepository.findAll().get(0);
    List<ItemDTO> items = this.itemsRepository.findAll().stream().map(entity -> {
      ItemDTO dto = new ItemDTO();
      dto.setId(entity.getId().toString());
      dto.setName(entity.getName());
      dto.setBarcode(entity.getBarcode());
      dto.setPrice(entity.getPrice());
      return dto;
    }).toList();

    com.personal.springlessons.model.dto.OrderItemsDTO order =
        new com.personal.springlessons.model.dto.OrderItemsDTO();
    order.setId(orderEntity.getId().toString());
    order.setItems(items);

    this.restTestClient.method(org.springframework.http.HttpMethod.DELETE)
        .uri(this.buildUrl("/v1/items")).contentType(MediaType.APPLICATION_JSON)
        .headers(this.retrieveHttpHeaders(this.validToken)).body(order).exchange().expectStatus()
        .isNoContent();
  }

  @Test
  void givenEmptyCollection_whenGetAll_thenNoContent() {
    this.cleanupItems();
    this.cleanupOrders();

    this.restTestClient.get().uri(this.buildUrl("/v1/items"))
        .headers(this.retrieveHttpHeaders(this.validToken)).exchange().expectStatus().isNoContent();
  }

  @Test
  void givenItems_whenGetAll_thenItemsRetrieved() {
    this.restTestClient.get().uri(this.buildUrl("/v1/items"))
        .headers(this.retrieveHttpHeaders(this.validToken)).exchange().expectStatus().isOk();
  }
}

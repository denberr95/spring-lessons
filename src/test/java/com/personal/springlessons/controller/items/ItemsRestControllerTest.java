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
    // TODO
  }

  @Test
  void givenWithoutAccessToken_whenCallAPI_thenReturnUnauthorized() {
    // TODO
  }

  @Test
  void givenItems_whenUpload_thenNoContent() {
    // TODO
  }

  @Test
  void givenItems_whenDelete_thenNoContent() {
    // TODO
  }

  @Test
  void givenEmptyCollection_whenGetAll_thenNoContent() {
    // TODO
  }

  @Test
  void givenItems_whenGetAll_thenItemsRetrieved() {
    // TODO
  }
}

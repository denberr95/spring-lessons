package com.personal.springlessons.component.runner;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@SpringBootTest
class AccountsRunnerTest {

  @Autowired
  private AccountsRunner accountsRunner;

  private RestClient wiremockAdmin;

  @BeforeEach
  void init() {
    this.wiremockAdmin = RestClient.builder().baseUrl("http://localhost:9998/__admin").build();
    this.wiremockAdmin.post().uri("/reset").retrieve().toBodilessEntity();
  }

  private void stubAccounts(int status, String body) {
    Map<String, Object> request = new HashMap<>();
    request.put("method", "GET");
    request.put("url", "/wiremock/accounts");

    Map<String, Object> response = new HashMap<>();
    response.put("status", status);
    if (body != null) {
      response.put("body", body);
      Map<String, String> headers = new HashMap<>();
      headers.put("Content-Type", "application/json");
      response.put("headers", headers);
    }

    Map<String, Object> stub = new HashMap<>();
    stub.put("request", request);
    stub.put("response", response);

    this.wiremockAdmin.post().uri("/mappings").contentType(MediaType.APPLICATION_JSON).body(stub)
        .retrieve().toBodilessEntity();
  }

  @Test
  void givenAccountsReturnedByClient_whenRun_thenEmailSentForEachAccount() {
    String accountsJson =
        "[{\"id\":\"1\",\"name\":\"Test\",\"surname\":\"User\",\"nationality\":\"IT\","
            + "\"email\":\"test@example.com\",\"phone\":\"1234\",\"address\":\"addr\","
            + "\"job\":\"dev\",\"createdAt\":\"2024-01-01T00:00:00Z\"}]";
    this.stubAccounts(200, accountsJson);

    assertDoesNotThrow(() -> this.accountsRunner.run(null));
  }

  @Test
  void givenNullResponseFromClient_whenRun_thenNoEmailSent() {
    this.stubAccounts(204, null);

    assertDoesNotThrow(() -> this.accountsRunner.run(null));
  }

  @Test
  void givenEmptyAccountList_whenRun_thenNoEmailSent() {
    this.stubAccounts(200, "[]");

    assertDoesNotThrow(() -> this.accountsRunner.run(null));
  }
}

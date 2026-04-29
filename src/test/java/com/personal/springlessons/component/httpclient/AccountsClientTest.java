package com.personal.springlessons.component.httpclient;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import com.personal.springlessons.model.dto.external.AccountDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

@SpringBootTest
class AccountsClientTest {

  @Autowired
  private AccountsClient accountsClient;

  @BeforeEach
  void init() {
    RestClient wiremockAdmin =
        RestClient.builder().baseUrl("http://localhost:9998/__admin").build();
    wiremockAdmin.post().uri("/mappings/reset").retrieve().toBodilessEntity();
  }

  @Test
  void whenGetAccounts_thenRetrieveAccountsDTO() {
    ResponseEntity<List<AccountDTO>> response = this.accountsClient.getAccounts();
    assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();

    List<AccountDTO> accounts = response.getBody();
    assertThat(accounts).isNotNull().hasSizeGreaterThan(0);
  }
}

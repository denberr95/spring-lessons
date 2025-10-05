package com.personal.springlessons.component.httpclient;

import java.util.List;

import com.personal.springlessons.model.dto.external.AccountDTO;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class AccountsClient {

  private final RestClient restClient;

  public AccountsClient(RestClient restClient) {
    this.restClient = restClient;
  }

  public ResponseEntity<List<AccountDTO>> getAccounts() {
    return this.restClient.method(HttpMethod.GET).uri("/accounts").retrieve()
        .toEntity(new ParameterizedTypeReference<>() {});
  }
}

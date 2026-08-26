package com.personal.springlessons.component.httpclient;

import java.util.List;

import com.personal.springlessons.model.dto.external.AccountDTO;

import org.springframework.http.ResponseEntity;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange("/accounts")
public interface IAccountsClient {

  @GetExchange
  ResponseEntity<List<AccountDTO>> getAccounts();
}

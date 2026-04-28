package com.personal.springlessons.component.runner;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AccountsRunnerTest {

  @Test
  void givenAccountsReturnedByClient_whenRun_thenEmailSentForEachAccount() {
    // TODO
  }

  @Test
  void givenNullResponseFromClient_whenRun_thenNoEmailSent() {
    // TODO
  }

  @Test
  void givenEmptyAccountList_whenRun_thenNoEmailSent() {
    // TODO
  }
}

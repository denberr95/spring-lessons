package com.personal.springlessons.component.httpclient;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import com.personal.springlessons.model.dto.external.AccountDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

@SpringBootTest
class AccountsClientTest {

    @Autowired
    private AccountsClient accountsClient;

    @Test
    void whenGetAccounts_thenRetrieveAccountsDTO() {
        ResponseEntity<List<AccountDTO>> response = this.accountsClient.getAccounts();
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();

        List<AccountDTO> accounts = response.getBody();
        assertThat(accounts).isNotNull().hasSizeGreaterThan(0);
    }
}

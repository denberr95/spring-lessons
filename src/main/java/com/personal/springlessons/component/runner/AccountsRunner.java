package com.personal.springlessons.component.runner;

import java.util.List;
import com.personal.springlessons.component.httpclient.AccountsClient;
import com.personal.springlessons.model.dto.external.AccountDTO;
import com.personal.springlessons.service.EmailService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.annotation.NewSpan;

@Component
public class AccountsRunner implements ApplicationRunner {

    private final AccountsClient accountsClient;
    private final EmailService emailService;
    private final Tracer tracer;

    public AccountsRunner(AccountsClient accountsClient, EmailService emailService, Tracer tracer) {
        this.accountsClient = accountsClient;
        this.emailService = emailService;
        this.tracer = tracer;
    }

    @NewSpan
    @Override
    public void run(ApplicationArguments args) throws Exception {
        Span currentSpan = this.tracer.currentSpan();
        ResponseEntity<List<AccountDTO>> response = this.accountsClient.getAccounts();
        List<AccountDTO> accounts = (response != null) ? response.getBody() : null;
        if (accounts != null) {
            accounts.forEach(this.emailService::sendEmail);
        }
        currentSpan.event("Application runner executed");
    }
}

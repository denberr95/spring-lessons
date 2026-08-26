package com.personal.springlessons.component.runner;

import java.util.List;

import com.personal.springlessons.component.httpclient.IAccountsClient;
import com.personal.springlessons.model.dto.external.AccountDTO;
import com.personal.springlessons.service.email.EmailService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.annotation.NewSpan;

@Component
public class AccountsRunner implements ApplicationRunner {

  private final IAccountsClient accountsClient;
  private final EmailService emailService;
  private final Tracer tracer;
  private static final Logger log = LoggerFactory.getLogger(AccountsRunner.class);

  public AccountsRunner(IAccountsClient accountsClient, EmailService emailService, Tracer tracer) {
    this.accountsClient = accountsClient;
    this.emailService = emailService;
    this.tracer = tracer;
  }

  @NewSpan(name = "accounts-runner")
  @Override
  public void run(ApplicationArguments args) throws Exception {
    log.info("Executing application runner to fetch accounts and send emails...");
    Span currentSpan = this.tracer.currentSpan();
    List<AccountDTO> accounts = this.accountsClient.getAccounts().getBody();
    if (accounts != null) {
      accounts.forEach(this.emailService::sendEmail);
    }
    log.info("Application runner execution completed");
    if (currentSpan != null) {
      currentSpan.event("Application runner executed");
    }
  }
}

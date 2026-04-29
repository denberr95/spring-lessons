package com.personal.springlessons.service.email;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.personal.springlessons.exception.SpringLessonsApplicationException;
import com.personal.springlessons.model.dto.external.AccountDTO;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EmailServiceTest {

  @Autowired
  private EmailService emailService;

  @Test
  void givenValidAccount_whenSendEmail_thenEmailIsSent() {
    AccountDTO account =
        new AccountDTO("1", "Test", "User", "IT", "test@example.com", "0000", "addr", "dev", null);

    assertDoesNotThrow(() -> this.emailService.sendEmail(account));
  }

  @Test
  void givenMailSenderFailure_whenSendEmail_thenThrowSpringLessonsApplicationException() {
    AccountDTO account = new AccountDTO(null, "Test", "User", null, null, null, null, null, null);

    assertThrows(SpringLessonsApplicationException.class,
        () -> this.emailService.sendEmail(account));
  }
}

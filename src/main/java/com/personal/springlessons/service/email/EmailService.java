package com.personal.springlessons.service.email;

import com.personal.springlessons.model.dto.external.AccountDTO;
import com.personal.springlessons.util.Constants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.annotation.NewSpan;

@Service
public class EmailService {

  private static final Logger log = LoggerFactory.getLogger(EmailService.class);
  private final JavaMailSender javaMailSender;
  private final Tracer tracer;

  public EmailService(JavaMailSender javaMailSender, Tracer tracer) {
    this.javaMailSender = javaMailSender;
    this.tracer = tracer;
  }

  @NewSpan
  public void sendEmail(final AccountDTO accountDTO) {
    log.info("Sending email to account: '{}'", accountDTO);
    Span currentSpan = this.tracer.currentSpan();
    try {
      SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
      simpleMailMessage.setFrom("spring-lessons@email.com");
      simpleMailMessage.setTo(accountDTO.email());
      simpleMailMessage.setSubject("Welcome to Spring Lessons");
      simpleMailMessage.setText("Hello " + accountDTO.name() + accountDTO.surname());
      this.javaMailSender.send(simpleMailMessage);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    log.info("Email sent to: '{}'", accountDTO.email());
    currentSpan.tag(Constants.SPAN_KEY_EMAIL, accountDTO.email());
    currentSpan.event("Email sent");
  }
}

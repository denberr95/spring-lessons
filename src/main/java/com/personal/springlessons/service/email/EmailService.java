package com.personal.springlessons.service.email;

import com.personal.springlessons.exception.SpringLessonsApplicationException;
import com.personal.springlessons.model.dto.external.AccountDTO;
import com.personal.springlessons.util.Constants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.annotation.Observed;

@Service
public class EmailService {

  private static final Logger log = LoggerFactory.getLogger(EmailService.class);
  private final JavaMailSender javaMailSender;
  private final ObservationRegistry observationRegistry;

  public EmailService(JavaMailSender javaMailSender, ObservationRegistry observationRegistry) {
    this.javaMailSender = javaMailSender;
    this.observationRegistry = observationRegistry;
  }

  @Observed(name = "email.send", contextualName = "send-email")
  public void sendEmail(final AccountDTO accountDTO) {
    log.info("Sending email to account: '{}'", accountDTO);

    Observation currentObservation = this.observationRegistry.getCurrentObservation();

    if (currentObservation != null && accountDTO.email() != null) {
      currentObservation.highCardinalityKeyValue(Constants.SPAN_KEY_EMAIL_TO, accountDTO.email());
    }

    try {

      SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
      simpleMailMessage.setFrom("spring-lessons@email.com");
      simpleMailMessage.setTo(accountDTO.email());
      simpleMailMessage.setSubject("Welcome to Spring Lessons");
      simpleMailMessage.setText("Hello " + accountDTO.name() + " " + accountDTO.surname());
      this.javaMailSender.send(simpleMailMessage);
      log.info("Email notified to: '{}'", accountDTO.email());

    } catch (Exception e) {
      throw new SpringLessonsApplicationException("Failed to send email to: " + accountDTO.email(),
          e);
    }
  }
}

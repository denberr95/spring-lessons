package com.personal.springlessons.component.event;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;

@SpringBootTest
class SuccessAuthenticationEventTest {

  @Autowired
  private ApplicationEventPublisher eventPublisher;

  @Test
  void givenSuccessAuthEvent_whenOnSuccess_thenEventIsHandled() {
    UsernamePasswordAuthenticationToken auth =
        new UsernamePasswordAuthenticationToken("user", "password", List.of());
    AuthenticationSuccessEvent event = new AuthenticationSuccessEvent(auth);

    assertDoesNotThrow(() -> this.eventPublisher.publishEvent(event));
  }
}

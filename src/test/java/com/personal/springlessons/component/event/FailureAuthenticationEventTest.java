package com.personal.springlessons.component.event;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;

@SpringBootTest
class FailureAuthenticationEventTest {

  @Autowired
  private ApplicationEventPublisher eventPublisher;

  @Test
  void givenFailureAuthEvent_whenOnFailure_thenEventIsHandled() {
    BadCredentialsException exception = new BadCredentialsException("bad credentials");
    UsernamePasswordAuthenticationToken auth =
        new UsernamePasswordAuthenticationToken("user", "password");
    AuthenticationFailureBadCredentialsEvent event =
        new AuthenticationFailureBadCredentialsEvent(auth, exception);

    assertDoesNotThrow(() -> this.eventPublisher.publishEvent(event));
  }
}

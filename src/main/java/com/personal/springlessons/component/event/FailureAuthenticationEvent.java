package com.personal.springlessons.component.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.stereotype.Component;

@Component
public class FailureAuthenticationEvent {

  private static final Logger log = LoggerFactory.getLogger(FailureAuthenticationEvent.class);

  @EventListener
  public void onFailure(AbstractAuthenticationFailureEvent failures) {
    log.trace("Failure authentication event: '{}'",
        failures.getException().getClass().getSimpleName());
    log.trace("Failure authentication issue: '{}'", failures.getException().getMessage());
    log.trace("Failure authentication request details: '{}'",
        failures.getException().getAuthenticationRequest());
  }
}

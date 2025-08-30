package com.personal.springlessons.component.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class SuccessAuthenticationEvent {

    private static final Logger log = LoggerFactory.getLogger(SuccessAuthenticationEvent.class);

    @EventListener
    public void onSuccess(AuthenticationSuccessEvent success) {
        log.trace("Success authentication event: '{}'",
                success.getSource().getClass().getSimpleName());
        log.trace("Success authentication details: '{}'", success.getAuthentication().getDetails());
        log.trace("Success authentication principal: '{}'", success.getAuthentication().getName());
        log.trace("Success authentication permissions: '{}'",
                success.getAuthentication().getAuthorities());
    }
}

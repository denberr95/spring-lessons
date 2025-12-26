package com.personal.springlessons.model.entity.revision;

import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;

import com.personal.springlessons.util.Constants;
import com.personal.springlessons.util.Methods;

import org.hibernate.envers.RevisionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class CustomRevisionEntityListener implements RevisionListener {

  private static final Logger log = LoggerFactory.getLogger(CustomRevisionEntityListener.class);

  @Override
  public void newRevision(Object revisionEntity) {
    log.debug("Adding additional information to revision");

    CustomRevisionEntity rev = (CustomRevisionEntity) revisionEntity;

    getCurrentRequest().map(this::resolveIpAddress).ifPresent(rev::setIpAddress);
    getCurrentRequest().map(this::resolveRequestUri).ifPresent(rev::setRequestUri);
    getCurrentRequest().map(this::resolveHttpMethod).ifPresent(rev::setHttpMethod);

    rev.setClientId(this.resolveClientId());
    rev.setUsername(this.resolveUsername());

    log.debug("Revision enrichment completed");
  }

  private static Optional<HttpServletRequest> getCurrentRequest() {
    return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
        .filter(ServletRequestAttributes.class::isInstance)
        .map(ServletRequestAttributes.class::cast).map(ServletRequestAttributes::getRequest);
  }

  private String resolveHttpMethod(HttpServletRequest request) {
    String result = request.getMethod();
    if (result == null || result.isBlank()) {
      result = Constants.S_UNKNOWN_HTTP_METHOD;
    }
    log.debug("Resolved HTTP method: '{}'", result);
    return result;
  }

  private String resolveRequestUri(HttpServletRequest request) {
    String result = request.getRequestURI();
    if (result == null || result.isBlank()) {
      result = Constants.S_UNKNOWN_REQUEST_URI;
    }
    log.debug("Resolved request URI: '{}'", result);
    return result;
  }

  private String resolveIpAddress(HttpServletRequest request) {
    String result = Methods.firstNonBlank(request.getHeader(Constants.S_X_FORWARDED_FOR),
        request.getRemoteAddr(), Constants.S_UNKNOWN_IP_ADDRESS);
    log.debug("Resolved IP address: '{}'", result);
    return result;
  }

  private String resolveClientId() {
    String result = null;
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
      result = Methods.firstNonBlank(jwt.getClaimAsString(Constants.S_AZP),
          jwt.getClaimAsString(Constants.S_CLIENT_ID), Constants.S_UNKNOWN_CLIENT_ID);
    }
    log.debug("Resolved clientId: '{}'", result);
    return result;
  }

  private String resolveUsername() {
    String result = null;
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
      result = Methods.firstNonBlank(jwt.getClaimAsString(Constants.S_SUB),
          jwt.getClaimAsString(Constants.S_PREFERRED_USERNAME), Constants.S_UNKNOWN_USERNAME);
    }
    log.debug("Resolved username: '{}'", result);
    return result;
  }
}

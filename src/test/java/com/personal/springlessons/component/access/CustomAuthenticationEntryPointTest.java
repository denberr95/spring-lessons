package com.personal.springlessons.component.access;

import static org.junit.jupiter.api.Assertions.assertEquals;

import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;

class CustomAuthenticationEntryPointTest {

  private final CustomAuthenticationEntryPoint entryPoint = new CustomAuthenticationEntryPoint();

  @Test
  void givenAuthenticationException_whenCommence_thenRespondWithUnauthorized() throws Exception {
    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();

    this.entryPoint.commence(request, response, new BadCredentialsException("Bad credentials"));

    assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
  }
}

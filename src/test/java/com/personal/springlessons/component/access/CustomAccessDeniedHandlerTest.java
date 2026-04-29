package com.personal.springlessons.component.access;

import static org.junit.jupiter.api.Assertions.assertEquals;

import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.access.AccessDeniedException;

class CustomAccessDeniedHandlerTest {

  private final CustomAccessDeniedHandler handler = new CustomAccessDeniedHandler();

  @Test
  void givenAccessDeniedException_whenHandle_thenRespondWithForbidden() throws Exception {
    MockHttpServletRequest request = new MockHttpServletRequest("GET", "/spring-app/v1/books");
    MockHttpServletResponse response = new MockHttpServletResponse();

    this.handler.handle(request, response, new AccessDeniedException("Access denied"));

    assertEquals(HttpServletResponse.SC_FORBIDDEN, response.getStatus());
  }
}

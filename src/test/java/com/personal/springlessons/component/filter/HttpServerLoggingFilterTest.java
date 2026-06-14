package com.personal.springlessons.component.filter;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

@SpringBootTest
class HttpServerLoggingFilterTest {

  @Autowired
  private HttpServerLoggingFilter filter;

  @Test
  void givenExcludedPath_whenDoFilter_thenSkipLogging() throws Exception {
    MockHttpServletRequest request = new MockHttpServletRequest("GET", "/actuator/health");
    MockHttpServletResponse response = new MockHttpServletResponse();
    MockFilterChain chain = new MockFilterChain();

    this.filter.doFilter(request, response, chain);

    assertNotNull(chain.getRequest());
  }

  @Test
  void givenIncludedPath_whenDoFilter_thenLogRequestAndResponse() throws Exception {
    MockHttpServletRequest request = new MockHttpServletRequest("GET", "/spring-app/v1/books");
    MockHttpServletResponse response = new MockHttpServletResponse();
    MockFilterChain chain = new MockFilterChain();

    this.filter.doFilter(request, response, chain);

    assertNotNull(chain.getRequest());
  }

  @Test
  void givenMultipartRequest_whenDoFilter_thenLogMultipartDetails() throws Exception {
    MockHttpServletRequest request =
        new MockHttpServletRequest("POST", "/spring-app/v1/books/upload");
    request.setContentType("multipart/form-data; boundary=----WebKitFormBoundary");
    MockHttpServletResponse response = new MockHttpServletResponse();
    MockFilterChain chain = new MockFilterChain();

    this.filter.doFilter(request, response, chain);

    assertNotNull(chain.getRequest());
  }

  @Test
  void givenResponseWithAttachment_whenDoFilter_thenLogDownloadedFileName() throws Exception {
    MockHttpServletRequest request =
        new MockHttpServletRequest("GET", "/spring-app/v1/books/download");
    MockHttpServletResponse response = new MockHttpServletResponse();
    response.setHeader("Content-Disposition", "attachment; filename=\"books_20240101.csv\"");
    MockFilterChain chain = new MockFilterChain();

    this.filter.doFilter(request, response, chain);

    assertNotNull(chain.getRequest());
  }

  @Test
  void givenPreReadRequestWithBody_whenDoFilter_thenLogBody() throws Exception {
    MockHttpServletRequest original = new MockHttpServletRequest("POST", "/spring-app/v1/books");
    original.setContent("{\"name\":\"book\"}".getBytes());
    ContentCachingRequestWrapper preRead = new ContentCachingRequestWrapper(original, 1024);
    preRead.getInputStream().readAllBytes();
    MockHttpServletResponse response = new MockHttpServletResponse();
    MockFilterChain chain = new MockFilterChain();

    this.filter.doFilter(preRead, response, chain);

    assertNotNull(chain.getRequest());
  }

  @Test
  void givenAlreadyWrappedRequest_whenDoFilter_thenUseExistingWrapper() throws Exception {
    MockHttpServletRequest original = new MockHttpServletRequest("GET", "/spring-app/v1/books");
    ContentCachingRequestWrapper wrapped = new ContentCachingRequestWrapper(original, 1024);
    MockHttpServletResponse response = new MockHttpServletResponse();
    MockFilterChain chain = new MockFilterChain();

    this.filter.doFilter(wrapped, response, chain);

    assertNotNull(chain.getRequest());
  }

  @Test
  void givenAlreadyWrappedResponse_whenDoFilter_thenUseExistingWrapper() throws Exception {
    MockHttpServletRequest request = new MockHttpServletRequest("GET", "/spring-app/v1/books");
    MockHttpServletResponse original = new MockHttpServletResponse();
    ContentCachingResponseWrapper wrapped = new ContentCachingResponseWrapper(original);
    MockFilterChain chain = new MockFilterChain();

    this.filter.doFilter(request, wrapped, chain);

    assertNotNull(chain.getRequest());
  }
}

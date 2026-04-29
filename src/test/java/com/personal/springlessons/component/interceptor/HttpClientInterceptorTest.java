package com.personal.springlessons.component.interceptor;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.URI;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.mock.http.client.MockClientHttpRequest;
import org.springframework.mock.http.client.MockClientHttpResponse;

class HttpClientInterceptorTest {

  private final HttpClientInterceptor interceptor = new HttpClientInterceptor();

  @Test
  void givenHttpRequest_whenIntercept_thenLogRequestAndReturnResponse() throws Exception {
    MockClientHttpRequest request =
        new MockClientHttpRequest(HttpMethod.GET, URI.create("http://localhost/test"));
    byte[] body = new byte[0];
    ClientHttpResponse response = new MockClientHttpResponse(new byte[0], HttpStatus.OK);

    ClientHttpResponse result = this.interceptor.intercept(request, body, (req, bd) -> response);

    assertNotNull(result);
  }

  @Test
  void givenMultipartRequest_whenIntercept_thenLogMultipartDetails() throws Exception {
    MockClientHttpRequest request =
        new MockClientHttpRequest(HttpMethod.POST, URI.create("http://localhost/upload"));
    HttpHeaders headers = new HttpHeaders();
    headers.set("Content-Type", "multipart/form-data; boundary=----boundary");
    headers.set("Content-Disposition", "form-data; filename=\"test.csv\"");
    request.getHeaders().addAll(headers);
    byte[] body = "file content".getBytes(StandardCharsets.UTF_8);
    ClientHttpResponse response = new MockClientHttpResponse(new byte[0], HttpStatus.OK);

    ClientHttpResponse result = this.interceptor.intercept(request, body, (req, bd) -> response);

    assertNotNull(result);
  }
}

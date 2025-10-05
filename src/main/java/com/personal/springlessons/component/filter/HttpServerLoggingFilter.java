package com.personal.springlessons.component.filter;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import com.personal.springlessons.util.Constants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Component
public class HttpServerLoggingFilter extends OncePerRequestFilter {

  private static final Logger log = LoggerFactory.getLogger(HttpServerLoggingFilter.class);

  @Value("${management.server.base-path:/actuator}")
  private String managementServerBasePath;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    ContentCachingRequestWrapper requestWrapper = this.requestWrapper(request);
    ContentCachingResponseWrapper responseWrapper = this.responseWrapper(response);
    if (!request.getRequestURI().startsWith(this.managementServerBasePath)) {
      this.logRequest(requestWrapper);
      filterChain.doFilter(requestWrapper, responseWrapper);
      this.logResponse(responseWrapper);
    }
  }

  private void logRequest(ContentCachingRequestWrapper request)
      throws ServletException, IOException {
    if (log.isInfoEnabled()) {
      log.info("--- HTTP Server Request ---");
      log.info("URI: '{}'", request.getRequestURI());
      log.info("Method: '{}'", request.getMethod());
      log.info("Headers: {}",
          this.getHeadersAsString(Collections.list(request.getHeaderNames()), request::getHeader));
      if (this.isMultipart(request.getContentType())) {
        this.logMultipartFiles(request);
      }
      if (request.getContentAsByteArray().length > 0) {
        log.info("Body: '{}'", new String(request.getContentAsByteArray()));
      }
      log.info("--- HTTP Server Request ---");
    }
  }

  private void logResponse(ContentCachingResponseWrapper response) throws IOException {
    if (log.isInfoEnabled()) {
      log.info("--- HTTP Server Response ---");
      log.info("Status Code: '{}'", response.getStatus());
      log.info("Headers: {}",
          this.getHeadersAsString(response.getHeaderNames(), response::getHeader));
      if (this.downloadedFile(response)) {
        this.logDownloadFile(response);
      }
      if (response.getContentAsByteArray().length > 0) {
        log.info("Body: '{}'", new String(response.getContentAsByteArray()));
      }
      response.copyBodyToResponse();
      log.info("--- HTTP Server Response ---");
    }
  }

  private boolean isMultipart(String contentType) {
    return contentType != null && contentType.startsWith(MediaType.MULTIPART_FORM_DATA_VALUE);
  }

  private boolean downloadedFile(ContentCachingResponseWrapper response) {
    return Optional.ofNullable(response.getHeader(HttpHeaders.CONTENT_DISPOSITION))
        .map(h -> h.contains("attachment")).orElse(false);
  }

  private void logMultipartFiles(HttpServletRequest request) throws ServletException, IOException {
    for (Part p : request.getParts()) {
      log.info("Uploaded File Name: '{}'", p.getSubmittedFileName());
      log.info("File Size: '{}'", p.getSize());
      log.info("Content Type: '{}'", p.getContentType());
    }
  }

  private void logDownloadFile(ContentCachingResponseWrapper response) {
    String contentDisposition = response.getHeader(HttpHeaders.CONTENT_DISPOSITION);
    if (contentDisposition != null && contentDisposition.contains("filename")) {
      String fileName = contentDisposition.substring(contentDisposition.indexOf("filename=") + 9)
          .replace(Constants.S_DOUBLE_QUOTE, Constants.S_EMPTY);
      log.info("Downloaded File Name: '{}'", fileName);
    }
  }

  private String getHeadersAsString(Collection<String> headerNames,
      UnaryOperator<String> headerValueResolver) {
    return headerNames.stream().map(headerName -> {
      String headerValue = Objects.toString(headerValueResolver.apply(headerName), "");
      return String.format("'%s' = '%s'", headerName, headerValue);
    }).collect(Collectors.joining(", "));
  }

  private ContentCachingRequestWrapper requestWrapper(ServletRequest request) {
    if (request instanceof ContentCachingRequestWrapper requestWrapper) {
      return requestWrapper;
    }
    return new ContentCachingRequestWrapper((HttpServletRequest) request);
  }

  private ContentCachingResponseWrapper responseWrapper(ServletResponse response) {
    if (response instanceof ContentCachingResponseWrapper responseWrapper) {
      return responseWrapper;
    }
    return new ContentCachingResponseWrapper((HttpServletResponse) response);
  }
}

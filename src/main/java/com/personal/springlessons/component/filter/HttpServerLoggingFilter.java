package com.personal.springlessons.component.filter;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.function.UnaryOperator;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class HttpServerLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        ContentCachingRequestWrapper requestWrapper = this.requestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = this.responseWrapper(response);
        this.logRequest(requestWrapper);
        filterChain.doFilter(requestWrapper, responseWrapper);
        this.logResponse(responseWrapper);
    }

    private void logRequest(ContentCachingRequestWrapper request) throws IOException {
        log.info("Http Server Request URI: '{}'", request.getRequestURI());
        log.info("Http Server Request Headers: {}", this.getHeadersAsString(
                Collections.list(request.getHeaderNames()), request::getHeader));
        if (this.isMultipart(request.getContentType())) {
            this.logMultipartFiles(request);
        } else {
            log.info("Http Server Request Body: '{}'", new String(request.getContentAsByteArray()));
        }
    }

    private void logResponse(ContentCachingResponseWrapper response) throws IOException {
        log.info("Http Server Response Status Code: '{}'", response.getStatus());
        log.info("Http Server Response Headers: {}",
                this.getHeadersAsString(response.getHeaderNames(), response::getHeader));
        if (this.isMultipart(response.getContentType())) {
            this.logDownloadFile(response);
        } else {
            log.info("Http Server Response Body: '{}'",
                    new String(response.getContentAsByteArray()));
        }
        response.copyBodyToResponse();
    }

    private boolean isMultipart(String contentType) {
        return contentType != null && contentType.startsWith(MediaType.MULTIPART_FORM_DATA_VALUE);
    }

    private void logMultipartFiles(HttpServletRequest request) {
        if (request instanceof MultipartHttpServletRequest multipartRequest) {
            for (MultipartFile file : multipartRequest.getFileMap().values()) {
                log.info("Http Server Request File Name: '{}'", file.getOriginalFilename());
            }
        }
    }

    private void logDownloadFile(ContentCachingResponseWrapper response) {
        String contentDisposition = response.getHeader(HttpHeaders.CONTENT_DISPOSITION);
        if (contentDisposition != null && contentDisposition.contains("attachment")) {
            String fileName = contentDisposition
                    .substring(contentDisposition.indexOf("filename=") + 9).replace("\"", "");
            log.info("Http Server Response File Name: '{}'", fileName);
        }
    }

    private String getHeadersAsString(Collection<String> headerNames,
            UnaryOperator<String> headerValueResolver) {
        StringBuilder headersBuilder = new StringBuilder();
        for (String headerName : headerNames) {
            String headerValue = headerValueResolver.apply(headerName);
            headersBuilder.append(String.format("'%s' = '%s', ", headerName, headerValue));
        }
        if (headersBuilder.length() > 0) {
            headersBuilder.setLength(headersBuilder.length() - 2);
        }
        return headersBuilder.toString();
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

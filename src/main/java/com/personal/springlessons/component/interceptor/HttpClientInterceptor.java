package com.personal.springlessons.component.interceptor;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import com.personal.springlessons.util.Constants;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class HttpClientInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body,
            ClientHttpRequestExecution execution) throws IOException {
        this.logRequest(request, body);
        ClientHttpResponse response = execution.execute(request, body);
        this.logResponse(response);
        return response;
    }

    private void logRequest(HttpRequest request, byte[] body) throws IOException {
        log.info("--- HTTP Client Request ---");
        log.info("URI: '{}'", request.getURI());
        log.info("Method: '{}'", request.getMethod());
        log.info("Headers: '{}'", request.getHeaders());

        if (this.isMultipartData(request.getHeaders())) {
            this.logMultipartDetails(request.getHeaders());
        } else {
            String requestBody = new String(body, StandardCharsets.UTF_8).replaceAll("\\s+", " ");
            log.info("Body: '{}'", requestBody);
        }
        log.info("--- HTTP Client Request ---");
    }

    private void logResponse(ClientHttpResponse response) throws IOException {
        log.info("--- HTTP Client Response ---");
        log.info("Status Code: '{}'", response.getStatusCode());
        log.info("Headers: '{}'", response.getHeaders());

        if (this.isMultipartData(response.getHeaders())) {
            this.logMultipartDetails(response.getHeaders());
        } else {
            String responseBody =
                    StreamUtils.copyToString(response.getBody(), StandardCharsets.UTF_8);
            log.info("Body: '{}'", responseBody);
        }
        log.info("--- HTTP Client Response ---");
    }

    private boolean isMultipartData(HttpHeaders headers) {
        MediaType contentType = headers.getContentType();
        return contentType != null && MediaType.MULTIPART_FORM_DATA.includes(contentType);
    }

    private void logMultipartDetails(HttpHeaders headers) {
        headers.forEach((headerName, headerValues) -> {
            if (HttpHeaders.CONTENT_DISPOSITION.equalsIgnoreCase(headerName)) {
                String fileName =
                        headerValues.stream().filter(value -> value.contains("filename"))
                                .map(value -> value.split("filename=")[1]
                                        .replace(Constants.S_DOUBLE_QUOTE, ""))
                                .findFirst().orElse("Unknown Filename");
                log.info("File Name: '{}'", fileName);
            }
        });
    }
}

package com.personal.springlessons.config;

import java.nio.file.Paths;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import com.personal.springlessons.util.Constants;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@Validated
@Configuration(proxyBeanMethods = false)
@ConfigurationProperties(prefix = "app-config")
public final class AppPropertiesConfig {

    private String baseDir;

    @NotNull(message = "API Documentation properties cannot be null")
    @Valid
    private ApiDocumentation apiDocumentation;

    @NotNull(message = "CSV Metadata properties cannot be null")
    @Valid
    private CsvMetadata csvMetadata;

    @NotNull(message = "API Client properties cannot be null")
    @Valid
    private ApiClient apiClient;

    @Data
    @Validated
    public static class ApiDocumentation {

        @NotBlank(message = "Token URL property cannot be null or empty")
        private String tokenUrl;
    }

    @Data
    @Validated
    public static class CsvMetadata {

        private String csvDir;

        @NotNull(message = "Column separator property cannot be null")
        private Character columnSeparator = Constants.C_SEMICOLON;

        @NotNull(message = "Quote character property cannot be null")
        private Character quoteCharacter = Constants.C_APOSTROPHE;

        @NotNull(message = "Ignore empty lines property cannot be null")
        private Boolean ignoreEmptyLines = Boolean.TRUE;

        @NotNull(message = "Ignore empty lines property cannot be null")
        private Boolean strictQuote = Boolean.TRUE;

        @NotNull(message = "Ignore empty lines property cannot be null")
        private Boolean applyAllQuotes = Boolean.FALSE;
    }

    @Data
    @Validated
    public static class ApiClient {

        @NotNull(message = "Connection request timeout property cannot be null")
        private Long connectionRequestTimeout = 5L;

        @NotNull(message = "Keep alive property cannot be null")
        private Long keepAlive = 30L;

        @NotNull(message = "Connection timeout property cannot be null")
        private Long connectionTimeout = 10L;

        @NotNull(message = "Socket timeout property cannot be null")
        private Long socketTimeout = 60L;

        @NotNull(message = "Time to Live property cannot be null")
        private Long timeToLive = 60L;

        @NotNull(message = "Max connection per route property cannot be null")
        private Integer maxConnPerRoute = 50;

        @NotNull(message = "Max connection total property cannot be null")
        private Integer maxConnTotal = 100;

        @NotNull(message = "Max redirects property cannot be null")
        private Integer maxRedirects = 5;

        @NotBlank(message = "Base URL property cannot be null or empty")
        private String baseUrl;
    }

    @PostConstruct
    public void init() {
        this.initCsvDir();
        log.debug("Custom Application Properties Loaded: '{}'", this.toString());
    }

    void initCsvDir() {
        this.csvMetadata.csvDir = Paths.get(this.baseDir, Constants.S_CSV).toString();
    }
}

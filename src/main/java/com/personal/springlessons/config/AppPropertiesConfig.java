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

    /**
     * The baseDir property is used to set the base directory for the application.
     */
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
    public static final class ApiDocumentation {

        /**
         * The title property is used to set the title for the API documentation.
         */
        @NotBlank(message = "Token URL property cannot be null or empty")
        private String tokenUrl;
    }

    @Data
    @Validated
    public static final class CsvMetadata {

        /**
         * The csvDir property is used to set the directory for the CSV metadata.
         */
        private String csvDir;

        /**
         * The header property is used to set the header for the CSV metadata.
         */
        @NotNull(message = "Column separator property cannot be null")
        private Character columnSeparator = Constants.C_SEMICOLON;

        /**
         * The quote character property is used to set the quote character for the CSV metadata.
         */
        @NotNull(message = "Quote character property cannot be null")
        private Character quoteCharacter = Constants.C_APOSTROPHE;

        /**
         * The escape character property is used to set the escape character for the CSV metadata.
         */
        @NotNull(message = "Ignore empty lines property cannot be null")
        private Boolean ignoreEmptyLines = Boolean.TRUE;

        /**
         * The strict quote property is used to set the strict quote for the CSV metadata.
         */
        @NotNull(message = "Ignore empty lines property cannot be null")
        private Boolean strictQuote = Boolean.TRUE;

        /**
         * The apply all quotes property is used to set the apply all quotes for the CSV metadata.
         */
        @NotNull(message = "Ignore empty lines property cannot be null")
        private Boolean applyAllQuotes = Boolean.FALSE;
    }

    @Data
    @Validated
    public static final class ApiClient {

        /**
         * The connection request timeout property is used to set the connection request timeout for
         * the connection pool.
         */
        @NotNull(message = "Connection request timeout property cannot be null")
        private Long connectionRequestTimeout = 5L;

        /**
         * The keep alive property is used to set the keep alive for the connection pool.
         */
        @NotNull(message = "Keep alive property cannot be null")
        private Long keepAlive = 30L;

        /**
         * The connection timeout property is used to set the connection timeout for the connection
         * pool.
         */
        @NotNull(message = "Connection timeout property cannot be null")
        private Long connectionTimeout = 10L;

        /**
         * The socket timeout property is used to set the socket timeout for the connection pool.
         */
        @NotNull(message = "Socket timeout property cannot be null")
        private Long socketTimeout = 60L;

        /**
         * The time to live property is used to set the time to live for the connection pool.
         */
        @NotNull(message = "Time to Live property cannot be null")
        private Long timeToLive = 60L;

        /**
         * The max connection per route property is used to set the maximum number of connections
         * that can be opened to the server.
         */
        @NotNull(message = "Max connection per route property cannot be null")
        private Integer maxConnPerRoute = 50;

        /**
         * The max connection total property is used to set the maximum number of connections that
         * can be opened to the server.
         */
        @NotNull(message = "Max connection total property cannot be null")
        private Integer maxConnTotal = 100;

        /**
         * The max redirects property is used to set the maximum number of redirects
         */
        @NotNull(message = "Max redirects property cannot be null")
        private Integer maxRedirects = 5;

        /**
         * The base URL property is used to set the base URL for the API client.
         */
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

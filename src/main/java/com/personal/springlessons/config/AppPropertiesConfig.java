package com.personal.springlessons.config;

import java.nio.file.Paths;

import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.personal.springlessons.util.Constants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import lombok.Data;

@Data
@Validated
@Configuration(proxyBeanMethods = false)
@ConfigurationProperties(prefix = "app-config")
public final class AppPropertiesConfig {

  private static final Logger log = LoggerFactory.getLogger(AppPropertiesConfig.class);

  /** This property is used to set the base directory for the application. */
  private String baseDir;

  @NotNull(message = "API Documentation properties cannot be null") @Valid private ApiDocumentation apiDocumentation;

  @NotNull(message = "CSV Metadata properties cannot be null") @Valid private CsvMetadata csvMetadata;

  @NotNull(message = "API Client properties cannot be null") @Valid private ApiClient apiClient;

  @Data
  @Validated
  public static final class ApiDocumentation {

    /** This property is used to set the token url authentication for the API documentation. */
    @NotBlank(message = "Token URL property cannot be null or empty") private String tokenUrl;
  }

  @Data
  @Validated
  public static final class CsvMetadata {

    /** This property is used to set the directory for the CSV metadata. */
    private String csvDir;

    /** This property is used to set the column character separator for the CSV data. */
    @NotNull(message = "Column separator property cannot be null") private Character columnSeparator = Constants.C_SEMICOLON;

    /** This property is used to set the quote character for the CSV data. */
    @NotNull(message = "Quote character property cannot be null") private Character quoteCharacter = Constants.C_APOSTROPHE;

    /** This property is used to set the escape character for the CSV data. */
    @NotNull(message = "Ignore empty lines property cannot be null") private Boolean ignoreEmptyLines = Boolean.TRUE;

    /** This property is used to set the strict quote for the CSV data. */
    @NotNull(message = "Ignore empty lines property cannot be null") private Boolean strictQuote = Boolean.TRUE;

    /** This property is used to set the apply all quotes for the CSV data. */
    @NotNull(message = "Ignore empty lines property cannot be null") private Boolean applyAllQuotes = Boolean.FALSE;
  }

  @Data
  @Validated
  public static final class ApiClient {

    /** This property is used to set the connection request timeout for the connection pool. */
    @NotNull(message = "Connection request timeout property cannot be null") private Long connectionRequestTimeout = 5L;

    /** This property is used to set the keep alive for the connection pool. */
    @NotNull(message = "Keep alive property cannot be null") private Long keepAlive = 30L;

    /** This property is used to set the connection timeout for the connection pool. */
    @NotNull(message = "Connection timeout property cannot be null") private Long connectionTimeout = 10L;

    /** This property is used to set the socket timeout for the connection pool. */
    @NotNull(message = "Socket timeout property cannot be null") private Long socketTimeout = 60L;

    /** This property is used to set the time to live for the connection pool. */
    @NotNull(message = "Time to Live property cannot be null") private Long timeToLive = 60L;

    /**
     * This property is used to set the maximum number of connections that can be opened to the
     * server per route.
     */
    @NotNull(message = "Max connection per route property cannot be null") private Integer maxConnPerRoute = 50;

    /**
     * This property is used to set the maximum number of connections that can be opened to the
     * server.
     */
    @NotNull(message = "Max connection total property cannot be null") private Integer maxConnTotal = 100;

    /** This property is used to set the maximum number of redirects */
    @NotNull(message = "Max redirects property cannot be null") private Integer maxRedirects = 5;

    /** This property is used to set the base URL for the API client. */
    @NotBlank(message = "Base URL property cannot be null or empty") private String baseUrl;
  }

  @PostConstruct
  public void init() {
    this.initCsvDir();
    log.debug("Custom Application Properties Loaded: '{}'", this);
  }

  void initCsvDir() {
    this.csvMetadata.csvDir = Paths.get(this.baseDir, Constants.S_CSV).toString();
  }
}

package com.personal.springlessons.config;

import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import com.personal.springlessons.util.Constants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Validated
@Configuration(proxyBeanMethods = false)
@ConfigurationProperties(prefix = "app-config")
public final class AppPropertiesConfig {

  private static final Logger log = LoggerFactory.getLogger(AppPropertiesConfig.class);

  /** This property is used to set the base directory for the application. */
  @NotBlank(message = "Base directory property cannot be null or empty") private String baseDir;

  @NotNull(message = "API Documentation properties cannot be null") @Valid private ApiDocumentation apiDocumentation;

  @NotNull(message = "CSV Metadata properties cannot be null") @Valid private CsvMetadata csvMetadata;

  @NotNull(message = "API Client properties cannot be null") @Valid private ApiClient apiClient;

  @NotNull(message = "Logging Filter properties cannot be null") @Valid private LoggingFilter loggingFilter;

  public String getBaseDir() {
    return this.baseDir;
  }

  public void setBaseDir(String baseDir) {
    this.baseDir = baseDir;
  }

  public ApiDocumentation getApiDocumentation() {
    return this.apiDocumentation;
  }

  public void setApiDocumentation(ApiDocumentation apiDocumentation) {
    this.apiDocumentation = apiDocumentation;
  }

  public CsvMetadata getCsvMetadata() {
    return this.csvMetadata;
  }

  public void setCsvMetadata(CsvMetadata csvMetadata) {
    this.csvMetadata = csvMetadata;
  }

  public ApiClient getApiClient() {
    return this.apiClient;
  }

  public void setApiClient(ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  public LoggingFilter getLoggingFilter() {
    return this.loggingFilter;
  }

  public void setLoggingFilter(LoggingFilter loggingFilter) {
    this.loggingFilter = loggingFilter;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof AppPropertiesConfig that))
      return false;
    return Objects.equals(this.baseDir, that.baseDir)
        && Objects.equals(this.apiDocumentation, that.apiDocumentation)
        && Objects.equals(this.csvMetadata, that.csvMetadata)
        && Objects.equals(this.apiClient, that.apiClient)
        && Objects.equals(this.loggingFilter, that.loggingFilter);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.baseDir, this.apiDocumentation, this.csvMetadata, this.apiClient,
        this.loggingFilter);
  }

  @Override
  public String toString() {
    return "AppPropertiesConfig{" + "baseDir='" + this.baseDir + '\'' + ", apiDocumentation="
        + this.apiDocumentation + ", csvMetadata=" + this.csvMetadata + ", apiClient="
        + this.apiClient + ", loggingFilter=" + this.loggingFilter + '}';
  }

  @Validated
  public static final class ApiDocumentation {

    /** This property is used to set the token url authentication for the API documentation. */
    @NotBlank(message = "Token URL property cannot be null or empty") private String tokenUrl;

    public String getTokenUrl() {
      return this.tokenUrl;
    }

    public void setTokenUrl(String tokenUrl) {
      this.tokenUrl = tokenUrl;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o)
        return true;
      if (!(o instanceof ApiDocumentation that))
        return false;
      return Objects.equals(this.tokenUrl, that.tokenUrl);
    }

    @Override
    public int hashCode() {
      return Objects.hash(this.tokenUrl);
    }

    @Override
    public String toString() {
      return "ApiDocumentation{tokenUrl='" + this.tokenUrl + "'}";
    }
  }

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

    public String getCsvDir() {
      return this.csvDir;
    }

    public void setCsvDir(String csvDir) {
      this.csvDir = csvDir;
    }

    public Character getColumnSeparator() {
      return this.columnSeparator;
    }

    public void setColumnSeparator(Character columnSeparator) {
      this.columnSeparator = columnSeparator;
    }

    public Character getQuoteCharacter() {
      return this.quoteCharacter;
    }

    public void setQuoteCharacter(Character quoteCharacter) {
      this.quoteCharacter = quoteCharacter;
    }

    public Boolean getIgnoreEmptyLines() {
      return this.ignoreEmptyLines;
    }

    public void setIgnoreEmptyLines(Boolean ignoreEmptyLines) {
      this.ignoreEmptyLines = ignoreEmptyLines;
    }

    public Boolean getStrictQuote() {
      return this.strictQuote;
    }

    public void setStrictQuote(Boolean strictQuote) {
      this.strictQuote = strictQuote;
    }

    public Boolean getApplyAllQuotes() {
      return this.applyAllQuotes;
    }

    public void setApplyAllQuotes(Boolean applyAllQuotes) {
      this.applyAllQuotes = applyAllQuotes;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o)
        return true;
      if (!(o instanceof CsvMetadata that))
        return false;
      return Objects.equals(this.csvDir, that.csvDir)
          && Objects.equals(this.columnSeparator, that.columnSeparator)
          && Objects.equals(this.quoteCharacter, that.quoteCharacter)
          && Objects.equals(this.ignoreEmptyLines, that.ignoreEmptyLines)
          && Objects.equals(this.strictQuote, that.strictQuote)
          && Objects.equals(this.applyAllQuotes, that.applyAllQuotes);
    }

    @Override
    public int hashCode() {
      return Objects.hash(this.csvDir, this.columnSeparator, this.quoteCharacter,
          this.ignoreEmptyLines, this.strictQuote, this.applyAllQuotes);
    }

    @Override
    public String toString() {
      return "CsvMetadata{" + "csvDir='" + this.csvDir + '\'' + ", columnSeparator="
          + this.columnSeparator + ", quoteCharacter=" + this.quoteCharacter + ", ignoreEmptyLines="
          + this.ignoreEmptyLines + ", strictQuote=" + this.strictQuote + ", applyAllQuotes="
          + this.applyAllQuotes + '}';
    }
  }

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

    public Long getConnectionRequestTimeout() {
      return this.connectionRequestTimeout;
    }

    public void setConnectionRequestTimeout(Long connectionRequestTimeout) {
      this.connectionRequestTimeout = connectionRequestTimeout;
    }

    public Long getKeepAlive() {
      return this.keepAlive;
    }

    public void setKeepAlive(Long keepAlive) {
      this.keepAlive = keepAlive;
    }

    public Long getConnectionTimeout() {
      return this.connectionTimeout;
    }

    public void setConnectionTimeout(Long connectionTimeout) {
      this.connectionTimeout = connectionTimeout;
    }

    public Long getSocketTimeout() {
      return this.socketTimeout;
    }

    public void setSocketTimeout(Long socketTimeout) {
      this.socketTimeout = socketTimeout;
    }

    public Long getTimeToLive() {
      return this.timeToLive;
    }

    public void setTimeToLive(Long timeToLive) {
      this.timeToLive = timeToLive;
    }

    public Integer getMaxConnPerRoute() {
      return this.maxConnPerRoute;
    }

    public void setMaxConnPerRoute(Integer maxConnPerRoute) {
      this.maxConnPerRoute = maxConnPerRoute;
    }

    public Integer getMaxConnTotal() {
      return this.maxConnTotal;
    }

    public void setMaxConnTotal(Integer maxConnTotal) {
      this.maxConnTotal = maxConnTotal;
    }

    public Integer getMaxRedirects() {
      return this.maxRedirects;
    }

    public void setMaxRedirects(Integer maxRedirects) {
      this.maxRedirects = maxRedirects;
    }

    public String getBaseUrl() {
      return this.baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
      this.baseUrl = baseUrl;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o)
        return true;
      if (!(o instanceof ApiClient that))
        return false;
      return Objects.equals(this.connectionRequestTimeout, that.connectionRequestTimeout)
          && Objects.equals(this.keepAlive, that.keepAlive)
          && Objects.equals(this.connectionTimeout, that.connectionTimeout)
          && Objects.equals(this.socketTimeout, that.socketTimeout)
          && Objects.equals(this.timeToLive, that.timeToLive)
          && Objects.equals(this.maxConnPerRoute, that.maxConnPerRoute)
          && Objects.equals(this.maxConnTotal, that.maxConnTotal)
          && Objects.equals(this.maxRedirects, that.maxRedirects)
          && Objects.equals(this.baseUrl, that.baseUrl);
    }

    @Override
    public int hashCode() {
      return Objects.hash(this.connectionRequestTimeout, this.keepAlive, this.connectionTimeout,
          this.socketTimeout, this.timeToLive, this.maxConnPerRoute, this.maxConnTotal,
          this.maxRedirects, this.baseUrl);
    }

    @Override
    public String toString() {
      return "ApiClient{" + "connectionRequestTimeout=" + this.connectionRequestTimeout
          + ", keepAlive=" + this.keepAlive + ", connectionTimeout=" + this.connectionTimeout
          + ", socketTimeout=" + this.socketTimeout + ", timeToLive=" + this.timeToLive
          + ", maxConnPerRoute=" + this.maxConnPerRoute + ", maxConnTotal=" + this.maxConnTotal
          + ", maxRedirects=" + this.maxRedirects + ", baseUrl='" + this.baseUrl + '\'' + '}';
    }
  }

  @Validated
  public static class LoggingFilter {

    /** Flag for enable verbose http logs of http server */
    @NotNull(message = "Enable verbose http logging") private Boolean enableHttpServer = Boolean.FALSE;

    /** Base path to exclude from http server logs */
    private List<@NotBlank(message = "Base path cannot be null or blank") @Pattern(
        regexp = "^(?:/[a-zA-Z0-9-]++)++$",
        message = "Path must be in the form /base-path/service-url") String> excludePath;

    public Boolean getEnableHttpServer() {
      return this.enableHttpServer;
    }

    public void setEnableHttpServer(Boolean enableHttpServer) {
      this.enableHttpServer = enableHttpServer;
    }

    public List<String> getExcludePath() {
      return this.excludePath;
    }

    public void setExcludePath(List<String> excludePath) {
      this.excludePath = excludePath;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o)
        return true;
      if (!(o instanceof LoggingFilter that))
        return false;
      return Objects.equals(this.enableHttpServer, that.enableHttpServer)
          && Objects.equals(this.excludePath, that.excludePath);
    }

    @Override
    public int hashCode() {
      return Objects.hash(this.enableHttpServer, this.excludePath);
    }

    @Override
    public String toString() {
      return "LoggingFilter{" + "enableHttpServer=" + this.enableHttpServer + ", excludePath="
          + this.excludePath + '}';
    }
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

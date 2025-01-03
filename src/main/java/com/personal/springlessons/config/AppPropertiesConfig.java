package com.personal.springlessons.config;

import java.nio.file.Paths;
import jakarta.annotation.PostConstruct;
import com.personal.springlessons.util.Constants;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@Configuration(proxyBeanMethods = false)
@ConfigurationProperties(prefix = "app-config")
public final class AppPropertiesConfig {

    private String baseDir;
    private ApiDocumentation apiDocumentation;
    private CsvMetadata csvMetadata;
    private ApiClient apiClient;

    @Data
    public static class ApiDocumentation {
        private String tokenUrl;
    }

    @Data
    public static class CsvMetadata {
        private String csvDir;
        private char columnSeparator = Constants.SEMICOLON;
        private char quoteCharacter = Constants.APOSTROPHE;
    }

    @Data
    public static class ApiClient {
        private long connectionRequestTimeout;
        private long keepAlive;
        private long connectionTimeout;
        private long socketTimeout;
        private long timeToLive;
        private int maxConnPerRoute;
        private int maxConnTotal;
        private int maxRedirects;
        private String baseUrl;
    }

    @PostConstruct
    public void init() {
        this.initCsvDir();
        log.debug("Custom Application Properties Loaded: '{}'", this.toString());
    }

    void initCsvDir() {
        this.csvMetadata.csvDir = Paths.get(this.baseDir, Constants.CSV).toString();
    }
}

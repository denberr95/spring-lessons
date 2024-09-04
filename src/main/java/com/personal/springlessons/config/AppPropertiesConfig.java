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
@ConfigurationProperties(prefix = "app-config", ignoreUnknownFields = true)
public final class AppPropertiesConfig {

    private String baseDir;
    private String csvDir;
    private char csvColumnSeparator = Constants.SEMICOLON;
    private char quoteCharacter = Constants.APOSTROPHE;

    @PostConstruct
    public void init() {
        this.initCsvDir();
        log.debug("Custom Application Properties Loaded: '{}'", this.toString());
    }

    void initCsvDir() {
        this.csvDir = Paths.get(this.baseDir, Constants.CSV).toString();
    }
}

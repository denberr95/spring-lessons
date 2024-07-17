package com.personal.springlessons.config;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@Configuration(proxyBeanMethods = false)
@ConfigurationProperties(prefix = "app-config")
public class AppPropertiesConfig {

    /*
     * Default path for file system
     */
    private String defaultPath;

    @PostConstruct
    public void init() {
        log.debug("Custom Application Properties Loaded: '{}'", this.toString());
    }
}

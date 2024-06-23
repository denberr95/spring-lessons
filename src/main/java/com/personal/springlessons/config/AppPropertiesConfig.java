package com.personal.springlessons.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import lombok.Data;

@Data
@Configuration(proxyBeanMethods = false)
@ConfigurationProperties(prefix = "app-config")
public class AppPropertiesConfig {

    /*
     * Default path for file system
     */
    private String defaultPath;
}

package com.personal.springlessons.config;

import com.personal.springlessons.util.Constants;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration(proxyBeanMethods = false)
public class KafkaTopicsConfig {

    @Bean
    NewTopic items() {
        return TopicBuilder.name(Constants.TOPIC_ITEMS).build();
    }
}

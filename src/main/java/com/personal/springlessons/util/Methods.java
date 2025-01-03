package com.personal.springlessons.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.UUID;
import com.personal.springlessons.exception.InvalidUUIDException;
import com.personal.springlessons.model.lov.DomainCategory;
import org.springframework.boot.info.GitProperties;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class Methods {

    private Methods() {
        throw new UnsupportedOperationException(
                "This is a utility class and cannot be instantiated");
    }

    public static UUID idValidation(String id) {
        log.debug("UUID to validate: '{}'", id);
        UUID result = null;
        try {
            result = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            throw new InvalidUUIDException(id);
        }
        log.debug("UUID: '{}' is valid", id);
        return result;
    }

    public static String dateTimeFormatter(String format, LocalDateTime localDateTime) {
        log.debug("DateTime to format: '{}' with format: '{}'", localDateTime, format);
        String result = null;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format);
        result = dtf.format(localDateTime);
        log.debug("DateTime formatted: '{}'", result);
        return result;
    }

    public static DomainCategory retrieveDomainCategory(String uri) {
        log.debug("Check Domain Category from uri: '{}'", uri);
        DomainCategory result = DomainCategory.ND;
        for (DomainCategory e : DomainCategory.values()) {
            if (uri.toLowerCase().contains(e.getValue().toLowerCase())) {
                result = e;
            }
        }
        log.debug("Domain Category retrieved: '{}'", result);
        return result;
    }

    public static Message<Object> createKafkaMessage(Object payload, String topic) {
        Message<Object> result = null;
        log.debug("Generate kafka message: '{}' to send at topic: '{}'", payload, topic);
        result = MessageBuilder.withPayload(payload).setHeader(KafkaHeaders.TOPIC, topic).build();
        log.debug("Kafka Message generated: '{}'", result.toString());
        return result;
    }

    public static String getApplicationVersion(GitProperties gitProperties) {
        return "v%s".formatted(gitProperties.get("build.version"));
    }

    public static String calculateETag(String id, OffsetDateTime lastModifiedTime)
            throws NoSuchAlgorithmException {
        String result = null;
        String baseString = "%s-%s".formatted(id, lastModifiedTime);
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(baseString.getBytes());
        result = "\"" + Base64.getEncoder().encodeToString(hash) + "\"";
        return result;
    }
}

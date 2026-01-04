package com.personal.springlessons.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import com.personal.springlessons.exception.InvalidUUIDException;
import com.personal.springlessons.model.lov.DomainCategory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.info.GitProperties;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public final class Methods {

  private static final Logger log = LoggerFactory.getLogger(Methods.class);

  private Methods() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
  }

  public static UUID idValidation(String id) {
    log.debug("UUID to validate: '{}'", id);
    UUID result = null;
    if (id == null || id.isBlank()) {
      throw new InvalidUUIDException(id);
    }
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
    DomainCategory result = DomainCategory.NA;
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
    log.debug("Kafka Message generated: '{}'", result.getPayload());
    return result;
  }

  public static String getApplicationVersion(GitProperties gitProperties) {
    return "v%s".formatted(gitProperties.get("build.version"));
  }

  public static String createFile(String folder, String name, String fileExtension,
      boolean useTimestamp) throws IOException {
    log.debug("Create file with name: '{}', extension: '{}', timestamp: '{}'", name, fileExtension,
        useTimestamp);
    String fileName = generateFileName(name, fileExtension, useTimestamp);
    Path filePath = createFilePath(folder, fileName);
    String file = createFile(filePath);
    log.debug("File created: '{}'", file);
    return file;
  }

  public static String generateFileName(String name, String fileExtension, boolean useTimestamp) {
    String result = null;
    String tms = useTimestamp
        ? Methods.dateTimeFormatter(Constants.S_DATE_TIME_FORMAT_1, LocalDateTime.now())
        : null;
    if (null == tms) {
      result = name + fileExtension;
    } else {
      result = name + Constants.C_UNDERSCORE + tms + fileExtension;
    }
    return result;
  }

  public static boolean isValidFileType(String fileName, List<String> fileTypes) {
    log.debug("Check if file: '{}' is valid respect to available file types: '{}'", fileName,
        fileTypes);
    return fileTypes.stream().anyMatch(fileName::endsWith);
  }

  public static MultiValueMap<String, String> addEtag(Long version) {
    log.debug("Create ETag version: '{}'", version);
    MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
    headers.add(Constants.S_ETAG, Constants.S_DOUBLE_QUOTE + version + Constants.S_DOUBLE_QUOTE);
    return headers;
  }

  public static MultiValueMap<String, String> addTotalRecord(int total) {
    log.debug("Create X-Total-Records: '{}'", total);
    MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
    headers.add(Constants.S_X_TOTAL_RECORDS, String.valueOf(total));
    return headers;
  }

  public static String getEtag(MultiValueMap<String, String> map) {
    log.debug("Retrieve ETag value");
    String result = null;
    String etag = map.getFirst(Constants.S_ETAG);
    if (etag != null) {
      result = etag.replace(Constants.S_DOUBLE_QUOTE, Constants.S_EMPTY);
    }
    log.debug("ETag value retrieved: '{}'", result);
    return result;
  }

  public static String getEtag(String value) {
    log.debug("Retrieve ETag value: '{}'", value);
    String result = null;
    if (value != null) {
      result = value.replace(Constants.S_DOUBLE_QUOTE, Constants.S_EMPTY);
    }
    log.debug("ETag value retrieved: '{}'", result);
    return result;
  }

  public static String firstNonBlank(String... values) {
    log.debug("Retrieve first non blank value");
    String result = null;
    if (values != null && values.length > 0) {
      for (String value : values) {
        if (value != null && !value.isBlank()) {
          result = value;
          break;
        }
      }
    }
    log.debug("First non blank value retrieved: '{}'", result);
    return result;
  }

  public static OffsetDateTime convertInstantToOffsetDateTime(Long instantInMillis) {
    log.debug("Convert Instant: '{}' to OffsetDateTime", instantInMillis);
    OffsetDateTime result = null;
    if (instantInMillis != null) {
      result =
          Instant.ofEpochMilli(instantInMillis).atZone(ZoneId.systemDefault()).toOffsetDateTime();
    }
    log.debug("OffsetDateTime converted: '{}'", result);
    return result;
  }

  private static Path createFilePath(String folder, String fileName) throws IOException {
    Path filePath = Paths.get(folder, fileName);
    Files.createDirectories(filePath.getParent());
    return filePath;
  }

  private static String createFile(Path filePath) throws IOException {
    return Files.createFile(filePath).toString();
  }
}

package com.personal.springlessons.model.entity.books;

import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;

@Component
public class BooksEntityListener {

  private static final Logger log = LoggerFactory.getLogger(BooksEntityListener.class);
  private final Tracer tracer;

  public BooksEntityListener(Tracer tracer) {
    this.tracer = tracer;
  }

  @PostLoad
  private void postLoad(BooksEntity entity) {
    this.logInSpan("postLoad", entity);
  }

  @PreUpdate
  private void preUpdate(BooksEntity entity) {
    this.logInSpan("preUpdate", entity);
  }

  @PostUpdate
  private void postUpdate(BooksEntity entity) {
    this.logInSpan("postUpdate", entity);
  }

  @PrePersist
  private void prePersist(BooksEntity entity) {
    this.logInSpan("prePersist", entity);
  }

  @PostPersist
  private void postPersist(BooksEntity entity) {
    this.logInSpan("postPersist", entity);
  }

  @PreRemove
  private void preRemove(BooksEntity entity) {
    this.logInSpan("preRemove", entity);
  }

  @PostRemove
  private void postRemove(BooksEntity entity) {
    this.logInSpan("postRemove", entity);
  }

  private void logInSpan(String event, BooksEntity entity) {
    if (this.tracer.currentSpan() != null) {
      log.trace("{}: '{}' entity: '{}'", event, entity.getClass().getName(), entity);
    } else {
      Span span = this.tracer.nextSpan().name("jpa.entity-listener").start();
      try (var _ = this.tracer.withSpan(span)) {
        log.trace("{}: '{}' entity: '{}'", event, entity.getClass().getName(), entity);
      } finally {
        span.end();
      }
    }
  }
}

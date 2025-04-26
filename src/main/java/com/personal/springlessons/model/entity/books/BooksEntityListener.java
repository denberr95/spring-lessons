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

public class BooksEntityListener {

    private static final Logger log = LoggerFactory.getLogger(BooksEntityListener.class);

    @PostLoad
    private void postLoad(BooksEntity entity) {
        log.trace("postLoad: '{}' entity: '{}'", entity.getClass().getName(), entity);
    }

    @PreUpdate
    private void preUpdate(BooksEntity entity) {
        log.trace("preUpdate: '{}' entity: '{}'", entity.getClass().getName(), entity);
    }

    @PostUpdate
    private void postUpdate(BooksEntity entity) {
        log.trace("postUpdate: '{}' entity: '{}'", entity.getClass().getName(), entity);
    }

    @PrePersist
    private void prePersist(BooksEntity entity) {
        log.trace("prePersist: '{}' entity: '{}'", entity.getClass().getName(), entity);
    }

    @PostPersist
    private void postPersist(BooksEntity entity) {
        log.trace("postPersist: '{}' entity: '{}'", entity.getClass().getName(), entity);
    }

    @PreRemove
    private void preRemove(BooksEntity entity) {
        log.trace("preRemove: '{}' entity: '{}'", entity.getClass().getName(), entity);
    }

    @PostRemove
    private void postRemove(BooksEntity entity) {
        log.trace("postRemove: '{}' entity: '{}'", entity.getClass().getName(), entity);
    }
}

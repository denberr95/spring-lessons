package com.personal.springlessons.model.entity;

import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BooksEntityListener {

    @PostLoad
    private void postLoad(BooksEntity entity) {
        log.trace("postLoad book entity: '{}'", entity);
    }

    @PreUpdate
    private void preUpdate(BooksEntity entity) {
        log.trace("preUpdate book entity: '{}'", entity);
    }

    @PostUpdate
    private void postUpdate(BooksEntity entity) {
        log.trace("postUpdate book entity: '{}'", entity);
    }

    @PrePersist
    private void prePersist(BooksEntity entity) {
        log.trace("prePersist book entity: '{}'", entity);
    }

    @PostPersist
    private void postPersist(BooksEntity entity) {
        log.trace("postPersist book entity: '{}'", entity);
    }

    @PreRemove
    private void preRemove(BooksEntity entity) {
        log.trace("preRemove book entity: '{}'", entity);
    }

    @PostRemove
    private void postRemove(BooksEntity entity) {
        log.trace("postRemove book entity: '{}'", entity);
    }
}

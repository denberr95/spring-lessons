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
public class ItemStatusEntityListener {

    @PostLoad
    private void postLoad(ItemStatusEntity entity) {
        log.trace("postLoad item status entity: '{}'", entity);
    }

    @PreUpdate
    private void preUpdate(ItemStatusEntity entity) {
        log.trace("preUpdate item status entity: '{}'", entity);
    }

    @PostUpdate
    private void postUpdate(ItemStatusEntity entity) {
        log.trace("postUpdate item status entity: '{}'", entity);
    }

    @PrePersist
    private void prePersist(ItemStatusEntity entity) {
        log.trace("prePersist item status entity: '{}'", entity);
    }

    @PostPersist
    private void postPersist(ItemStatusEntity entity) {
        log.trace("postPersist item status entity: '{}'", entity);
    }

    @PreRemove
    private void preRemove(ItemStatusEntity entity) {
        log.trace("preRemove item status entity: '{}'", entity);
    }

    @PostRemove
    private void postRemove(ItemStatusEntity entity) {
        log.trace("postRemove item status entity: '{}'", entity);
    }
}

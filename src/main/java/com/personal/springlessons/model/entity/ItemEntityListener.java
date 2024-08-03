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
public class ItemEntityListener {

    @PostLoad
    private void postLoad(ItemEntity entity) {
        log.trace("postLoad item entity: '{}'", entity);
    }

    @PreUpdate
    private void preUpdate(ItemEntity entity) {
        log.trace("preUpdate item entity: '{}'", entity);
    }

    @PostUpdate
    private void postUpdate(ItemEntity entity) {
        log.trace("postUpdate item entity: '{}'", entity);
    }

    @PrePersist
    private void prePersist(ItemEntity entity) {
        log.trace("prePersist item entity: '{}'", entity);
    }

    @PostPersist
    private void postPersist(ItemEntity entity) {
        log.trace("postPersist item entity: '{}'", entity);
    }

    @PreRemove
    private void preRemove(ItemEntity entity) {
        log.trace("preRemove item entity: '{}'", entity);
    }

    @PostRemove
    private void postRemove(ItemEntity entity) {
        log.trace("postRemove item entity: '{}'", entity);
    }
}

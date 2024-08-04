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
public class ItemsEntityListener {

    @PostLoad
    private void postLoad(ItemsEntity entity) {
        log.trace("postLoad item entity: '{}'", entity);
    }

    @PreUpdate
    private void preUpdate(ItemsEntity entity) {
        log.trace("preUpdate item entity: '{}'", entity);
    }

    @PostUpdate
    private void postUpdate(ItemsEntity entity) {
        log.trace("postUpdate item entity: '{}'", entity);
    }

    @PrePersist
    private void prePersist(ItemsEntity entity) {
        log.trace("prePersist item entity: '{}'", entity);
    }

    @PostPersist
    private void postPersist(ItemsEntity entity) {
        log.trace("postPersist item entity: '{}'", entity);
    }

    @PreRemove
    private void preRemove(ItemsEntity entity) {
        log.trace("preRemove item entity: '{}'", entity);
    }

    @PostRemove
    private void postRemove(ItemsEntity entity) {
        log.trace("postRemove item entity: '{}'", entity);
    }
}

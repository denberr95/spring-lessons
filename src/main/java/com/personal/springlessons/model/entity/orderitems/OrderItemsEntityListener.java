package com.personal.springlessons.model.entity.orderitems;

import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OrderItemsEntityListener {

    @PostLoad
    private void postLoad(OrderItemsEntity entity) {
        log.trace("postLoad item status entity: '{}'", entity);
    }

    @PreUpdate
    private void preUpdate(OrderItemsEntity entity) {
        log.trace("preUpdate item status entity: '{}'", entity);
    }

    @PostUpdate
    private void postUpdate(OrderItemsEntity entity) {
        log.trace("postUpdate item status entity: '{}'", entity);
    }

    @PrePersist
    private void prePersist(OrderItemsEntity entity) {
        log.trace("prePersist item status entity: '{}'", entity);
    }

    @PostPersist
    private void postPersist(OrderItemsEntity entity) {
        log.trace("postPersist item status entity: '{}'", entity);
    }

    @PreRemove
    private void preRemove(OrderItemsEntity entity) {
        log.trace("preRemove item status entity: '{}'", entity);
    }

    @PostRemove
    private void postRemove(OrderItemsEntity entity) {
        log.trace("postRemove item status entity: '{}'", entity);
    }
}

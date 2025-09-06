package com.personal.springlessons.model.entity.orderitems;

import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderItemsEntityListener {

    private static final Logger log = LoggerFactory.getLogger(OrderItemsEntityListener.class);

    @PostLoad
    private void postLoad(OrderItemsEntity entity) {
        log.trace("postLoad: '{}' entity: '{}'", entity.getClass().getName(), entity);
    }

    @PreUpdate
    private void preUpdate(OrderItemsEntity entity) {
        log.trace("preUpdate: '{}' entity: '{}'", entity.getClass().getName(), entity);
    }

    @PostUpdate
    private void postUpdate(OrderItemsEntity entity) {
        log.trace("postUpdate: '{}' entity: '{}'", entity.getClass().getName(), entity);
    }

    @PrePersist
    private void prePersist(OrderItemsEntity entity) {
        log.trace("prePersist: '{}' entity: '{}'", entity.getClass().getName(), entity);
    }

    @PostPersist
    private void postPersist(OrderItemsEntity entity) {
        log.trace("postPersist: '{}' entity: '{}'", entity.getClass().getName(), entity);
    }

    @PreRemove
    private void preRemove(OrderItemsEntity entity) {
        log.trace("preRemove: '{}' entity: '{}'", entity.getClass().getName(), entity);
    }

    @PostRemove
    private void postRemove(OrderItemsEntity entity) {
        log.trace("postRemove: '{}' entity: '{}'", entity.getClass().getName(), entity);
    }
}

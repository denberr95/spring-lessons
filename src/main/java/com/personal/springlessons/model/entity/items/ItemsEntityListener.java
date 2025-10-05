package com.personal.springlessons.model.entity.items;

import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ItemsEntityListener {

  private static final Logger log = LoggerFactory.getLogger(ItemsEntityListener.class);

  @PostLoad
  private void postLoad(ItemsEntity entity) {
    log.trace("postLoad: '{}' entity: '{}'", entity.getClass().getName(), entity);
  }

  @PreUpdate
  private void preUpdate(ItemsEntity entity) {
    log.trace("preUpdate: '{}' entity: '{}'", entity.getClass().getName(), entity);
  }

  @PostUpdate
  private void postUpdate(ItemsEntity entity) {
    log.trace("postUpdate: '{}' entity: '{}'", entity.getClass().getName(), entity);
  }

  @PrePersist
  private void prePersist(ItemsEntity entity) {
    log.trace("prePersist: '{}' entity: '{}'", entity.getClass().getName(), entity);
  }

  @PostPersist
  private void postPersist(ItemsEntity entity) {
    log.trace("postPersist: '{}' entity: '{}'", entity.getClass().getName(), entity);
  }

  @PreRemove
  private void preRemove(ItemsEntity entity) {
    log.trace("preRemove: '{}' entity: '{}'", entity.getClass().getName(), entity);
  }

  @PostRemove
  private void postRemove(ItemsEntity entity) {
    log.trace("postRemove: '{}' entity: '{}'", entity.getClass().getName(), entity);
  }
}

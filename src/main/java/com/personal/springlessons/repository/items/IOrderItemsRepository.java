package com.personal.springlessons.repository.items;

import java.util.UUID;

import com.personal.springlessons.model.entity.items.OrderItemsEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IOrderItemsRepository extends JpaRepository<OrderItemsEntity, UUID> {

  @EntityGraph(attributePaths = "items")
  Page<OrderItemsEntity> findAll(Pageable pageable);
}

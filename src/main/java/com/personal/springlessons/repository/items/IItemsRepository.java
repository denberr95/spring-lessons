package com.personal.springlessons.repository.items;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.personal.springlessons.model.entity.items.ItemsEntity;
import com.personal.springlessons.model.entity.items.OrderItemsEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IItemsRepository extends JpaRepository<ItemsEntity, UUID> {

  Optional<ItemsEntity> findByBarcode(String barcode);

  Optional<List<ItemsEntity>> findByorderItemsEntity(OrderItemsEntity orderItemsEntity);
}

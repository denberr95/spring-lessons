package com.personal.springlessons.repository;

import java.util.Optional;
import java.util.UUID;
import com.personal.springlessons.model.entity.ItemsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IItemsRepository extends JpaRepository<ItemsEntity, UUID> {
    
    Optional<ItemsEntity> findByBarcode(String barcode);
}

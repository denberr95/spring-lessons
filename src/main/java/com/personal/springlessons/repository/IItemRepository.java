package com.personal.springlessons.repository;

import java.util.Optional;
import java.util.UUID;
import com.personal.springlessons.model.entity.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IItemRepository extends JpaRepository<ItemEntity, UUID> {
    
    Optional<ItemEntity> findByBarcode(String barcode);
}

package com.personal.springlessons.repository;

import java.util.UUID;
import com.personal.springlessons.model.entity.ItemStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IItemStatusRepository extends JpaRepository<ItemStatusEntity, UUID> {
    
}

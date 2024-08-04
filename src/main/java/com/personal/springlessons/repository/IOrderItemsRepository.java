package com.personal.springlessons.repository;

import java.util.UUID;
import com.personal.springlessons.model.entity.OrderItemsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IOrderItemsRepository extends JpaRepository<OrderItemsEntity, UUID> {
    
}

package com.personal.springlessons.repository;

import java.util.UUID;
import com.personal.springlessons.model.entity.OrderItemsEntity;
import com.personal.springlessons.model.lov.ItemStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface IOrderItemsRepository extends JpaRepository<OrderItemsEntity, UUID> {

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE OrderItemsEntity u SET u.status = :status WHERE u.id = :id")
    void updateStatusById(@Param(value = "status") ItemStatus status, @Param(value = "id") UUID id);
}

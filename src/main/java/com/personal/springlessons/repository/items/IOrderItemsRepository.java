package com.personal.springlessons.repository.items;

import java.util.UUID;
import com.personal.springlessons.model.entity.items.OrderItemsEntity;
import com.personal.springlessons.model.lov.ItemStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface IOrderItemsRepository extends JpaRepository<OrderItemsEntity, UUID> {

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("UPDATE OrderItemsEntity u SET u.status = :status WHERE u.id = :id")
    int updateStatusById(@Param(value = "status") ItemStatus status, @Param(value = "id") UUID id);
}

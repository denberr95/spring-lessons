package com.personal.springlessons.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.personal.springlessons.model.entity.OrderItemsEntity;
import com.personal.springlessons.model.lov.ItemStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
class IOrderItemsRepositoryTest {

    @Autowired
    private IOrderItemsRepository orderItemsRepository;

    private static final int TOTAL = 5;

    @BeforeEach
    void init() {
        for (int i = 0; i < TOTAL; i++) {
            OrderItemsEntity entity = new OrderItemsEntity();
            entity.setQuantity(9_999);
            entity.setStatus(ItemStatus.NA);
            this.orderItemsRepository.save(entity);
        }
    }

    @AfterEach
    void tearDown() {
        this.orderItemsRepository.deleteAll();
    }

    @Test
    void givenExistingOrderItemsId_whenUpdateStatus_thenStatusUpdated() {
        OrderItemsEntity row = this.orderItemsRepository.findAll().get(0);
        int totalRowsUpdated = this.orderItemsRepository.updateStatusById(ItemStatus.DISCARDED, row.getId());
        assertEquals(1, totalRowsUpdated);
        OrderItemsEntity actual = this.orderItemsRepository.findById(row.getId()).get();
        assertEquals(ItemStatus.DISCARDED, actual.getStatus());
    }
}

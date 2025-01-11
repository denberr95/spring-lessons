package com.personal.springlessons.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.math.BigDecimal;
import java.util.Optional;
import com.personal.springlessons.model.entity.items.ItemsEntity;
import com.personal.springlessons.model.entity.orderitems.OrderItemsEntity;
import com.personal.springlessons.model.lov.Channel;
import com.personal.springlessons.model.lov.ItemStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class IItemsRepositoryTest {

    @Autowired
    private IItemsRepository itemRepository;

    @Autowired
    private IOrderItemsRepository orderItemsRepository;

    private static final int TOTAL = 5;

    @BeforeEach
    void init() {
        OrderItemsEntity orderItemsEntity = new OrderItemsEntity();
        orderItemsEntity.setQuantity(TOTAL);
        orderItemsEntity.setStatus(ItemStatus.NA);
        orderItemsEntity.setChannel(Channel.NA);
        orderItemsEntity = this.orderItemsRepository.saveAndFlush(orderItemsEntity);
        for (int i = 0; i < TOTAL; i++) {
            ItemsEntity itemEntity = new ItemsEntity();
            itemEntity.setName("Repository-Item-Name-" + i);
            itemEntity.setBarcode("Reposity-Item-Barcode-" + i);
            itemEntity.setPrice(new BigDecimal("9999.99"));
            itemEntity.setOrderItemsEntity(orderItemsEntity);
            this.itemRepository.save(itemEntity);
        }
    }

    @AfterEach
    void tearDown() {
        this.itemRepository.deleteAll();
        this.orderItemsRepository.deleteAll();
    }

    @Test
    void givenExistentItem_whenFindByBarcode_thenItemRetrieved() {
        String barcode = "Reposity-Item-Barcode-0";
        Optional<ItemsEntity> result = this.itemRepository.findByBarcode(barcode);
        assertTrue(result.isPresent());
        assertEquals(barcode, result.get().getBarcode());
    }

    @Test
    void givenNonExistentItem_whenFindByBarcode_thenNotFound() {
        Optional<ItemsEntity> result = this.itemRepository.findByBarcode("Reposity-Item-Barcode");
        assertTrue(result.isEmpty());
    }
}

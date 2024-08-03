package com.personal.springlessons.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.math.BigDecimal;
import java.util.Optional;
import com.personal.springlessons.model.entity.ItemEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class IItemRepositoryTest {

    @Autowired
    private IItemRepository itemRepository;

    private static final int TOTAL = 5;

    @BeforeEach
    void init() {
        for (int i = 0; i < TOTAL; i++) {
            ItemEntity itemEntity = new ItemEntity();
            itemEntity.setName("Repository-Item-Name-" + i);
            itemEntity.setBarcode("Reposity-Item-Barcode-" + i);
            itemEntity.setPrice(new BigDecimal(9999.99));
            this.itemRepository.save(itemEntity);
        }
    }

    @AfterEach
    void tearDown() {
        this.itemRepository.deleteAll();
    }

    @Test
    void givenExistentItem_whenFindByBarcode_thenItemIsRetrieved() {
        String barcode = "Reposity-Item-Barcode-0";
        Optional<ItemEntity> result = this.itemRepository.findByBarcode(barcode);
        assertTrue(result.isPresent());
        assertEquals(barcode, result.get().getBarcode());
    }

    @Test
    void givenExistentItem_whenFindByBarcode_thenNotFound() {
        String barcode = "Reposity-Item-Barcode";
        Optional<ItemEntity> result = this.itemRepository.findByBarcode(barcode);
        assertTrue(result.isEmpty());
    }
}

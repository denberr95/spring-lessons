package com.personal.springlessons.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import com.personal.springlessons.model.dto.ItemDTO;
import com.personal.springlessons.model.dto.KafkaMessageItemDTO;
import com.personal.springlessons.repository.IOrderItemsRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootTest
class ItemsServiceTest {

    @Autowired
    private ItemsService itemsService;

    @Autowired
    private IOrderItemsRepository orderItemsRepository;

    @Autowired
    private KafkaTemplate<String, KafkaMessageItemDTO> kafkaTemplate;

    private static final int TOTAL = 5;
    private List<ItemDTO> items;

    @BeforeEach
    void initAll() {
        this.items = new ArrayList<>(TOTAL);
        for (int i = 0; i < TOTAL; i++) {
            ItemDTO itemDTO = new ItemDTO();
            itemDTO.setBarcode("Service-Barcode-" + i);
            itemDTO.setName("Service-Name-" + i);
            itemDTO.setPrice(new BigDecimal(9_999.99));
            this.items.add(itemDTO);
        }
    }

    @AfterEach
    void tearDown() {
        // TODO
    }

    @Test
    void testUpload() {
        // TODO
    }

    @Test
    void testDelete() {
        // TODO
    }
}

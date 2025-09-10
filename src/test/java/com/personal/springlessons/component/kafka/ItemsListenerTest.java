package com.personal.springlessons.component.kafka;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import com.personal.springlessons.model.dto.ItemDTO;
import com.personal.springlessons.model.entity.items.ItemsEntity;
import com.personal.springlessons.model.entity.orderitems.OrderItemsEntity;
import com.personal.springlessons.model.lov.Channel;
import com.personal.springlessons.repository.IItemsRepository;
import com.personal.springlessons.repository.IOrderItemsRepository;
import com.personal.springlessons.service.ItemsService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ItemsListenerTest {

    @Autowired
    private ItemsService itemsService;

    @Autowired
    private IOrderItemsRepository orderItemsRepository;

    @Autowired
    private IItemsRepository itemsRepository;

    private static final int TOTAL = 5;

    private List<ItemDTO> data = new ArrayList<>(TOTAL);

    @BeforeEach
    void init() {
        for (int i = 0; i < TOTAL; i++) {
            String name = "Listener-Item-Name-" + i;
            String barcode = "Listener-Barcode-" + i;
            BigDecimal price = new BigDecimal("9999.99");

            ItemDTO itemDTO = new ItemDTO();
            itemDTO.setName(name);
            itemDTO.setBarcode(barcode);
            itemDTO.setPrice(price);
            this.data.add(itemDTO);
        }
    }

    @AfterEach
    void tearDown() {
        this.orderItemsRepository.findAll()
                .forEach(order -> this.orderItemsRepository.delete(order));
    }

    @Test
    void givenNewItems_whenUploadItems_thenItemsShouldBeSaved() {
        this.itemsService.upload(this.data, Channel.NA);

        await().timeout(2L, TimeUnit.SECONDS).until(() -> this.orderItemsRepository.count(),
                equalTo(1L));
        OrderItemsEntity orderItemsEntity = this.orderItemsRepository.findAll().get(0);

        await().timeout(10L, TimeUnit.SECONDS).until(() -> this.itemsRepository.findAll().size(),
                equalTo(TOTAL));
        List<ItemsEntity> itemsEntity =
                this.itemsRepository.findByorderItemsEntity(orderItemsEntity).get();

        for (int i = 0; i < TOTAL; i++) {
            ItemsEntity element = itemsEntity.get(i);
            ItemDTO item = this.data.get(i);
            assertNotNull(element.getId());
            assertNotNull(element.getCreatedAt());
            assertEquals(item.getName(), element.getName());
            assertEquals(item.getBarcode(), element.getBarcode());
            assertEquals(item.getPrice(), element.getPrice());
        }
    }

    @Test
    void givenDuplicatedBarcode_whenUploadItems_thenDuplicatedBarcodeExceptionShouldBeThrown() {
        // TODO
    }

    @Test
    void givenExistingItems_whenDeleteItems_thenItemShouldBeDeleted() {
        // TODO
    }
}

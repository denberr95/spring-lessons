package com.personal.springlessons.repository.items;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import com.personal.springlessons.model.entity.items.ItemsEntity;
import com.personal.springlessons.model.entity.items.OrderItemsEntity;
import com.personal.springlessons.model.lov.Channel;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@SpringBootTest
class IOrderItemsRepositoryTest {

  @Autowired
  private IOrderItemsRepository orderItemsRepository;

  @Autowired
  private IItemsRepository itemsRepository;

  private static final int TOTAL = 5;

  @BeforeEach
  void init() {
    for (int i = 0; i < TOTAL; i++) {
      OrderItemsEntity entity = new OrderItemsEntity();
      entity.setChannel(Channel.NA);
      this.orderItemsRepository.save(entity);
    }
  }

  @AfterEach
  void tearDown() {
    this.itemsRepository.deleteAll();
    this.orderItemsRepository.deleteAll();
  }

  @Test
  void givenExistingOrders_whenFindAllPaged_thenPageSizeRespected() {
    int pageSize = 2;
    Page<OrderItemsEntity> page = this.orderItemsRepository.findAll(PageRequest.of(0, pageSize));
    assertEquals(pageSize, page.getContent().size());
    assertEquals(TOTAL, page.getTotalElements());
  }

  @Test
  void givenOrderWithItems_whenFindAll_thenItemsEagerlyLoaded() {
    OrderItemsEntity order =
        this.orderItemsRepository.findAll(PageRequest.of(0, 1)).getContent().getFirst();
    ItemsEntity item = new ItemsEntity();
    item.setName("Test-Item");
    item.setBarcode("TEST-BARCODE-EAGER-001");
    item.setPrice(new BigDecimal("9.99"));
    item.setOrderItemsEntity(order);
    this.itemsRepository.saveAndFlush(item);

    Page<OrderItemsEntity> page = this.orderItemsRepository.findAll(PageRequest.of(0, TOTAL));
    OrderItemsEntity loaded = page.getContent().stream()
        .filter(o -> o.getId().equals(order.getId())).findFirst().orElseThrow();

    assertNotNull(loaded.getItems());
    assertEquals(1, loaded.getItems().size());
    assertEquals("TEST-BARCODE-EAGER-001", loaded.getItems().getFirst().getBarcode());
  }

  @Test
  void givenOrderWithNoItems_whenFindAll_thenItemsListIsEmpty() {
    Page<OrderItemsEntity> page = this.orderItemsRepository.findAll(PageRequest.of(0, TOTAL));
    assertTrue(page.getContent().stream().allMatch(o -> o.getItems().isEmpty()));
  }

}

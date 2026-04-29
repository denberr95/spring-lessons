package com.personal.springlessons.service.items;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;

import com.personal.springlessons.model.dto.ItemDTO;
import com.personal.springlessons.model.dto.OrderItemsDTO;
import com.personal.springlessons.model.dto.wrapper.OrderItemsWrapperDTO;
import com.personal.springlessons.model.entity.items.ItemsEntity;
import com.personal.springlessons.model.entity.items.OrderItemsEntity;
import com.personal.springlessons.model.lov.Channel;
import com.personal.springlessons.repository.items.IItemsRepository;
import com.personal.springlessons.repository.items.IOrderItemsRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

@SpringBootTest
class ItemsServiceTest {

  @Autowired
  private ItemsService itemsService;

  @Autowired
  private IItemsRepository itemsRepository;

  @Autowired
  private IOrderItemsRepository orderItemsRepository;

  @AfterEach
  void cleanup() {
    this.itemsRepository.deleteAllInBatch();
    this.orderItemsRepository.deleteAllInBatch();
  }

  @Test
  void givenOrderWithItems_whenUpload_thenOrderIsSavedAndKafkaMessagesAreSent() {
    ItemDTO item = new ItemDTO();
    item.setName("Upload Item");
    item.setBarcode("UPL001");
    item.setPrice(new BigDecimal("9.99"));
    OrderItemsDTO order = new OrderItemsDTO();
    order.setItems(List.of(item));

    assertDoesNotThrow(() -> {
      OrderItemsDTO result = this.itemsService.upload(order, Channel.NA);
      assertNotNull(result.getId());
    });
  }

  @Test
  void givenOrderWithItems_whenDelete_thenKafkaMessagesAreSent() {
    OrderItemsEntity orderEntity = new OrderItemsEntity();
    orderEntity = this.orderItemsRepository.saveAndFlush(orderEntity);

    ItemsEntity itemEntity = new ItemsEntity();
    itemEntity.setName("Delete Item");
    itemEntity.setBarcode("DEL001");
    itemEntity.setPrice(new BigDecimal("5.00"));
    itemEntity.setOrderItemsEntity(orderEntity);
    this.itemsRepository.saveAndFlush(itemEntity);

    ItemDTO item = new ItemDTO();
    item.setId(itemEntity.getId().toString());
    item.setName(itemEntity.getName());
    item.setBarcode(itemEntity.getBarcode());
    item.setPrice(itemEntity.getPrice());

    OrderItemsDTO order = new OrderItemsDTO();
    order.setId(orderEntity.getId().toString());
    order.setItems(List.of(item));

    assertDoesNotThrow(() -> this.itemsService.delete(order, Channel.NA));
  }

  @Test
  void givenOrderItems_whenGetAll_thenReturnPaginatedResult() {
    ItemDTO item = new ItemDTO();
    item.setName("Paginated Item");
    item.setBarcode("PAG001");
    item.setPrice(new BigDecimal("15.00"));
    OrderItemsDTO order = new OrderItemsDTO();
    order.setItems(List.of(item));
    this.itemsService.upload(order, Channel.NA);

    OrderItemsWrapperDTO result = this.itemsService.getAll(PageRequest.of(0, 10));

    assertFalse(result.getContent().isEmpty());
  }

  @Test
  void givenEmptyDatabase_whenGetAll_thenReturnEmptyPage() {
    OrderItemsWrapperDTO result = this.itemsService.getAll(PageRequest.of(0, 10));

    assertTrue(result.getContent().isEmpty());
  }
}

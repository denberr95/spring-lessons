package com.personal.springlessons.component.kafka;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.personal.springlessons.model.dto.ItemDTO;
import com.personal.springlessons.repository.items.IItemsRepository;
import com.personal.springlessons.repository.items.IOrderItemsRepository;
import com.personal.springlessons.service.items.ItemsService;

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
    this.orderItemsRepository.findAll().forEach(order -> this.orderItemsRepository.delete(order));
  }

  @Test
  void givenNewItems_whenUploadItems_thenItemsShouldBeSaved() {
    // TODO
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

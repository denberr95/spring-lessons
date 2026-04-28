package com.personal.springlessons.component.kafka;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import com.personal.springlessons.config.AppPropertiesConfig;
import com.personal.springlessons.model.dto.ItemDTO;
import com.personal.springlessons.model.dto.OrderItemsDTO;
import com.personal.springlessons.model.lov.Channel;
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

  @Autowired
  private AppPropertiesConfig appPropertiesConfig;

  private static final int TOTAL = 5;

  private List<ItemDTO> data = new ArrayList<>(TOTAL);

  @BeforeEach
  void init() {
    this.data.clear();
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
  void tearDown() throws IOException {
    this.itemsRepository.deleteAllInBatch();
    this.orderItemsRepository.deleteAllInBatch();
    Path csvDir = Paths.get(this.appPropertiesConfig.getCsvMetadata().getCsvDir());
    if (Files.exists(csvDir)) {
      List<Path> files = Files.list(csvDir).toList();
      for (Path p : files) {
        Files.delete(p);
      }
    }
  }

  @Test
  void givenNewItems_whenUploadItems_thenItemsShouldBeSaved() {
    OrderItemsDTO order = new OrderItemsDTO();
    order.setItems(this.data);

    this.itemsService.upload(order, Channel.NA);

    await().atMost(10, TimeUnit.SECONDS).until(() -> this.itemsRepository.count() == TOTAL);

    assertThat(this.itemsRepository.count()).isEqualTo(TOTAL);
  }

  @Test
  void givenDuplicatedBarcode_whenUploadItems_thenDuplicatedBarcodeExceptionShouldBeThrown() {
    OrderItemsDTO order = new OrderItemsDTO();
    order.setItems(this.data);

    this.itemsService.upload(order, Channel.NA);
    await().atMost(10, TimeUnit.SECONDS).until(() -> this.itemsRepository.count() == TOTAL);

    this.itemsService.upload(order, Channel.NA);

    Path csvDir = Paths.get(this.appPropertiesConfig.getCsvMetadata().getCsvDir());
    await().atMost(10, TimeUnit.SECONDS).until(() -> {
      if (!Files.exists(csvDir)) {
        return false;
      }
      try (Stream<Path> files = Files.list(csvDir)) {
        return files.count() == TOTAL;
      }
    });

    assertThat(this.itemsRepository.count()).isEqualTo(TOTAL);
  }

  @Test
  void givenExistingItems_whenDeleteItems_thenItemShouldBeDeleted() {
    OrderItemsDTO order = new OrderItemsDTO();
    order.setItems(this.data);
    OrderItemsDTO saved = this.itemsService.upload(order, Channel.NA);

    await().atMost(10, TimeUnit.SECONDS).until(() -> this.itemsRepository.count() == TOTAL);

    List<ItemDTO> itemsWithIds = this.itemsRepository.findAll().stream().map(entity -> {
      ItemDTO item = new ItemDTO();
      item.setId(entity.getId().toString());
      item.setBarcode(entity.getBarcode());
      item.setName(entity.getName());
      item.setPrice(entity.getPrice());
      return item;
    }).toList();

    OrderItemsDTO deleteOrder = new OrderItemsDTO();
    deleteOrder.setId(saved.getId());
    deleteOrder.setItems(itemsWithIds);

    this.itemsService.delete(deleteOrder, Channel.NA);

    await().atMost(10, TimeUnit.SECONDS).until(() -> this.itemsRepository.count() == 0);

    assertThat(this.itemsRepository.count()).isZero();
  }
}

package com.personal.springlessons.service.items;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.personal.springlessons.exception.SpringLessonsApplicationException;
import com.personal.springlessons.model.dto.ItemDTO;
import com.personal.springlessons.model.dto.OrderItemsDTO;
import com.personal.springlessons.model.dto.response.KafkaMessageItemDTO;
import com.personal.springlessons.model.entity.items.OrderItemsEntity;
import com.personal.springlessons.model.lov.Channel;
import com.personal.springlessons.repository.items.IOrderItemsRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;

import io.micrometer.observation.ObservationRegistry;

@ExtendWith(MockitoExtension.class)
class ItemsServiceUnitTest {

  @Mock
  private IOrderItemsRepository orderItemsRepository;

  @Mock
  private KafkaTemplate<String, KafkaMessageItemDTO> kafkaTemplate;

  private ItemsService itemsService;

  @BeforeEach
  void setUp() {
    ObservationRegistry observationRegistry = ObservationRegistry.create();
    this.itemsService = new ItemsService(observationRegistry, orderItemsRepository, kafkaTemplate);
  }

  @Test
  @SuppressWarnings("unchecked")
  void givenKafkaSendThrows_whenUpload_thenThrowSpringLessonsApplicationException() {
    UUID orderId = UUID.randomUUID();
    OrderItemsEntity savedEntity = new OrderItemsEntity();
    savedEntity.setId(orderId);

    ItemDTO item = new ItemDTO();
    item.setBarcode("BARCODE-001");
    item.setName("Test Item");
    item.setPrice(new BigDecimal("9.99"));

    OrderItemsDTO order = new OrderItemsDTO();
    order.setItems(List.of(item));

    when(orderItemsRepository.saveAndFlush(any())).thenReturn(savedEntity);
    when(kafkaTemplate.send(any(Message.class))).thenThrow(new RuntimeException("kafka error"));

    assertThrows(SpringLessonsApplicationException.class,
        () -> itemsService.upload(order, Channel.POSTMAN));
  }
}

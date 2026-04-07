package com.personal.springlessons.service.items;

import java.util.List;

import com.personal.springlessons.exception.SpringLessonsApplicationException;
import com.personal.springlessons.model.dto.ItemDTO;
import com.personal.springlessons.model.dto.OrderItemsDTO;
import com.personal.springlessons.model.dto.response.KafkaMessageItemDTO;
import com.personal.springlessons.model.dto.wrapper.OrderItemsWrapperDTO;
import com.personal.springlessons.model.entity.items.OrderItemsEntity;
import com.personal.springlessons.model.lov.Channel;
import com.personal.springlessons.model.lov.ItemStatus;
import com.personal.springlessons.repository.items.IOrderItemsRepository;
import com.personal.springlessons.util.Constants;
import com.personal.springlessons.util.Methods;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.annotation.Observed;

@Service
public class ItemsService {

  private final ObservationRegistry observationRegistry;
  private final IOrderItemsRepository orderItemsRepository;
  private final KafkaTemplate<String, KafkaMessageItemDTO> kafkaTemplate;

  public ItemsService(ObservationRegistry observationRegistry,
      IOrderItemsRepository orderItemsRepository,
      KafkaTemplate<String, KafkaMessageItemDTO> kafkaTemplate) {
    this.observationRegistry = observationRegistry;
    this.orderItemsRepository = orderItemsRepository;
    this.kafkaTemplate = kafkaTemplate;
  }

  @Observed(name = "items.upload", contextualName = "upload-order-items")
  @Transactional
  public OrderItemsDTO upload(final OrderItemsDTO order, final Channel channel) {
    Observation current = this.observationRegistry.getCurrentObservation();
    if (current != null) {
      current.highCardinalityKeyValue(Constants.SPAN_KEY_NUMBER_ITEMS_TO_UPLOAD,
          String.valueOf(order.getItems().size()));
    }

    OrderItemsEntity data = new OrderItemsEntity();
    data.setChannel(channel);
    OrderItemsEntity saved = this.orderItemsRepository.saveAndFlush(data);

    order.getItems().forEach(i -> {
      KafkaMessageItemDTO message =
          this.buildKafkaMessage(i, ItemStatus.UPLOAD, saved.getId().toString());
      this.notifyKafkaItems(message, Constants.TOPIC_ITEMS);
    });

    OrderItemsDTO result = new OrderItemsDTO();
    result.setId(saved.getId().toString());
    result.setItems(order.getItems());
    return result;
  }

  @Observed(name = "items.delete", contextualName = "delete-order-items")
  public void delete(final OrderItemsDTO order, final Channel channel) {
    Observation current = this.observationRegistry.getCurrentObservation();
    if (current != null) {
      current.highCardinalityKeyValue(Constants.SPAN_KEY_NUMBER_ITEMS_TO_DELETE,
          String.valueOf(order.getItems().size()));
    }

    order.getItems().forEach(i -> {
      KafkaMessageItemDTO message = this.buildKafkaMessage(i, ItemStatus.DELETE, order.getId());
      message.setId(i.getId());
      this.notifyKafkaItems(message, Constants.TOPIC_ITEMS);
    });
  }

  @Observed(name = "items.retrieval", contextualName = "get-all-order-items")
  @Transactional(readOnly = true)
  public OrderItemsWrapperDTO getAll(Pageable pageable) {
    Page<OrderItemsEntity> page = this.orderItemsRepository.findAll(pageable);

    List<OrderItemsDTO> content = page.getContent().stream().map(orderEntity -> {
      OrderItemsDTO orderItemsDto = new OrderItemsDTO();
      orderItemsDto.setId(orderEntity.getId().toString());
      orderItemsDto.setQuantity(orderEntity.getItems().size());

      List<ItemDTO> itemsDto = orderEntity.getItems().stream().map(itemEntity -> {
        ItemDTO item = new ItemDTO();
        item.setId(itemEntity.getId().toString());
        item.setBarcode(itemEntity.getBarcode());
        item.setName(itemEntity.getName());
        item.setPrice(itemEntity.getPrice());
        return item;
      }).toList();

      orderItemsDto.setItems(itemsDto);
      return orderItemsDto;
    }).toList();

    OrderItemsWrapperDTO result = new OrderItemsWrapperDTO();
    result.setContent(content);
    result.setPage(page.getNumber());
    result.setSize(page.getSize());
    result.setTotalElements(page.getTotalElements());
    result.setTotalPages(page.getTotalPages());

    Observation current = this.observationRegistry.getCurrentObservation();
    if (current != null) {
      current
          .highCardinalityKeyValue(Constants.SPAN_KEY_TOTAL_ORDERS,
              String.valueOf(result.getTotalElements()))
          .highCardinalityKeyValue(Constants.SPAN_KEY_PAGE_NUMBER, String.valueOf(result.getPage()))
          .highCardinalityKeyValue(Constants.SPAN_KEY_PAGE_SIZE, String.valueOf(result.getSize()));
    }

    return result;
  }

  private void notifyKafkaItems(KafkaMessageItemDTO message, String topic) {
    Observation obs =
        Observation.createNotStarted("items.kafka.notification", this.observationRegistry)
            .contextualName("notify-kafka-message-items")
            .lowCardinalityKeyValue(Constants.OPERATION, message.getItemStatus().name())
            .highCardinalityKeyValue(Constants.SPAN_KEY_ID_ORDER_ITEMS, message.getIdOrderItems())
            .highCardinalityKeyValue(Constants.SPAN_KEY_BARCODE, message.getBarcode());
    obs.start();
    try {
      Message<Object> kafkaMessage = Methods.createKafkaMessage(message, topic);
      this.kafkaTemplate.send(kafkaMessage);
    } catch (Exception e) {
      obs.error(e);
      throw new SpringLessonsApplicationException(e);
    } finally {
      obs.stop();
    }
  }

  private KafkaMessageItemDTO buildKafkaMessage(ItemDTO item, ItemStatus status, String orderId) {
    KafkaMessageItemDTO message = new KafkaMessageItemDTO();
    message.setBarcode(item.getBarcode());
    message.setName(item.getName());
    message.setPrice(item.getPrice());
    message.setItemStatus(status);
    message.setIdOrderItems(orderId);
    return message;
  }
}

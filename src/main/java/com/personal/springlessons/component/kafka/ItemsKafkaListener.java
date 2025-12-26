package com.personal.springlessons.component.kafka;

import java.util.UUID;

import com.personal.springlessons.exception.items.DuplicatedBarcodeException;
import com.personal.springlessons.model.csv.DiscardedItemCsv;
import com.personal.springlessons.model.dto.response.KafkaMessageItemDTO;
import com.personal.springlessons.model.entity.items.ItemsEntity;
import com.personal.springlessons.model.entity.items.OrderItemsEntity;
import com.personal.springlessons.repository.items.IItemsRepository;
import com.personal.springlessons.repository.items.IOrderItemsRepository;
import com.personal.springlessons.util.Constants;
import com.personal.springlessons.util.Methods;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;

@Component
public class ItemsKafkaListener {

  private static final Logger log = LoggerFactory.getLogger(ItemsKafkaListener.class);
  private final ApplicationEventPublisher applicationEventPublisher;
  private final IItemsRepository itemRepository;
  private final IOrderItemsRepository iOrderItemsRepository;
  private final Tracer tracer;

  public ItemsKafkaListener(ApplicationEventPublisher applicationEventPublisher,
      IItemsRepository itemRepository, IOrderItemsRepository iOrderItemsRepository, Tracer tracer) {
    this.applicationEventPublisher = applicationEventPublisher;
    this.itemRepository = itemRepository;
    this.iOrderItemsRepository = iOrderItemsRepository;
    this.tracer = tracer;
  }

  @Transactional
  @RetryableTopic(attempts = Constants.S_VAL_1, dltStrategy = DltStrategy.NO_DLT)
  @KafkaListener(groupId = "upload-items.group", topics = Constants.TOPIC_ITEMS,
      filter = "uploadItemsRecordFilter", concurrency = Constants.S_VAL_3)
  public void upload(@Payload KafkaMessageItemDTO message) {
    Span span = this.tracer.nextSpan().name("process-kafka-upload");

    try (Tracer.SpanInScope ws = this.tracer.withSpan(span.start())) {
      log.info("Received item to upload: '{}'", message);

      span.tag(Constants.SPAN_KEY_BARCODE, message.getBarcode());

      this.itemRepository.findByBarcode(message.getBarcode()).ifPresent(item -> {
        log.warn("Barcode: '{}' already exists", item.getBarcode());

        DiscardedItemCsv event = new DiscardedItemCsv();
        event.setBarcode(item.getBarcode());
        event.setIdItem(item.getId().toString());
        event.setIdOrderItems(message.getIdOrderItems());
        event.setIdOrderItemsOriginal(item.getOrderItemsEntity().getId().toString());
        event.setName(message.getName());
        event.setPrice(message.getPrice());

        this.applicationEventPublisher.publishEvent(event);

        span.event("Duplicated barcode found, published event");
        throw new DuplicatedBarcodeException(message.getBarcode(), item.getId().toString());
      });

      ItemsEntity itemsEntity = new ItemsEntity();
      itemsEntity.setName(message.getName());
      itemsEntity.setPrice(message.getPrice());
      itemsEntity.setBarcode(message.getBarcode());

      UUID orderItemsId = Methods.idValidation(message.getIdOrderItems());
      OrderItemsEntity orderItemsEntity = this.iOrderItemsRepository.getReferenceById(orderItemsId);

      itemsEntity.setOrderItemsEntity(orderItemsEntity);

      this.itemRepository.saveAndFlush(itemsEntity);

      span.tag(Constants.SPAN_KEY_ID_ITEMS, itemsEntity.getId().toString());
      span.event("Saved item successfully");
    } finally {
      span.end();
    }
  }

  @Transactional
  @RetryableTopic(attempts = Constants.S_VAL_1, dltStrategy = DltStrategy.NO_DLT)
  @KafkaListener(groupId = "delete-items.group", topics = Constants.TOPIC_ITEMS,
      filter = "deleteItemsRecordFilter", concurrency = Constants.S_VAL_3)
  public void delete(@Payload KafkaMessageItemDTO message) {
    Span span = this.tracer.nextSpan().name("process-kafka-delete");

    try (Tracer.SpanInScope ws = this.tracer.withSpan(span.start())) {
      log.info("Received item to delete: '{}'", message);

      span.tag(Constants.SPAN_KEY_ID_ORDER_ITEMS, message.getIdOrderItems());
      span.tag(Constants.SPAN_KEY_ID_ITEMS, message.getId());
      span.tag(Constants.SPAN_KEY_BARCODE, message.getBarcode());

      UUID idItem = Methods.idValidation(message.getId());

      this.itemRepository.findById(idItem).ifPresent(item -> {
        OrderItemsEntity orderItemsEntity = item.getOrderItemsEntity();
        if (orderItemsEntity != null) {
          orderItemsEntity.getItems().remove(item);
          span.event("Removed item successfully");
          if (orderItemsEntity.getItems().isEmpty()) {
            log.info("No more items for OrderItemsEntity '{}' deleting parent",
                orderItemsEntity.getId());
            this.iOrderItemsRepository.delete(orderItemsEntity);
            span.event("Removed order items successfully");
          }
        }
      });
    } finally {
      span.end();
    }
  }
}

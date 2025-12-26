package com.personal.springlessons.service.items;

import java.util.List;

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

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.annotation.NewSpan;

@Service
public class ItemsService {

  private final Tracer tracer;
  private final IOrderItemsRepository orderItemsRepository;
  private final KafkaTemplate<String, KafkaMessageItemDTO> kafkaTemplate;

  public ItemsService(Tracer tracer, IOrderItemsRepository orderItemsRepository,
      KafkaTemplate<String, KafkaMessageItemDTO> kafkaTemplate) {
    this.tracer = tracer;
    this.orderItemsRepository = orderItemsRepository;
    this.kafkaTemplate = kafkaTemplate;
  }

  @NewSpan
  public OrderItemsDTO upload(final OrderItemsDTO order, final Channel channel) {
    Span currentSpan = this.tracer.currentSpan();
    currentSpan.tag(Constants.SPAN_KEY_NUMBER_ITEMS_TO_UPLOAD, order.getItems().size());

    OrderItemsDTO result = new OrderItemsDTO();

    OrderItemsEntity data = new OrderItemsEntity();
    data.setChannel(channel);
    OrderItemsEntity saved = this.orderItemsRepository.saveAndFlush(data);

    result.setId(saved.getId().toString());
    result.setItems(order.getItems());

    order.getItems().forEach(i -> {

      KafkaMessageItemDTO message = new KafkaMessageItemDTO();
      message.setBarcode(i.getBarcode());
      message.setName(i.getName());
      message.setPrice(i.getPrice());
      message.setItemStatus(ItemStatus.UPLOAD);
      message.setIdOrderItems(saved.getId().toString());

      this.notifyKafkaItems(message, Constants.TOPIC_ITEMS);
    });

    return result;
  }

  @NewSpan
  public void delete(final OrderItemsDTO order, final Channel channel) {
    Span currentSpan = this.tracer.currentSpan();
    currentSpan.tag(Constants.SPAN_KEY_NUMBER_ITEMS_TO_DELETE, order.getItems().size());

    order.getItems().forEach(i -> {

      KafkaMessageItemDTO message = new KafkaMessageItemDTO();
      message.setBarcode(i.getBarcode());
      message.setName(i.getName());
      message.setPrice(i.getPrice());
      message.setItemStatus(ItemStatus.DELETE);
      message.setIdOrderItems(order.getId());
      message.setId(i.getId());

      this.notifyKafkaItems(message, Constants.TOPIC_ITEMS);
    });
  }

  @Transactional(readOnly = true)
  @NewSpan
  public OrderItemsWrapperDTO getAll(Pageable pageable) {
    Span currentSpan = this.tracer.currentSpan();
    OrderItemsWrapperDTO result = new OrderItemsWrapperDTO();
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

    result.setContent(content);
    result.setPage(page.getNumber());
    result.setSize(page.getSize());
    result.setTotalElements(page.getTotalElements());
    result.setTotalPages(page.getTotalPages());

    currentSpan.tag(Constants.SPAN_KEY_TOTAL_ORDERS, String.valueOf(result.getTotalElements()))
        .tag(Constants.SPAN_KEY_PAGE_NUMBER, String.valueOf(result.getPage()))
        .tag(Constants.SPAN_KEY_PAGE_SIZE, String.valueOf(result.getSize()))
        .event("Orders retrieved (paginated)");

    return result;
  }

  private void notifyKafkaItems(KafkaMessageItemDTO message, String topic) {
    Span span = this.tracer.nextSpan().name("notify-kafka-message-items");
    try (Tracer.SpanInScope ws = this.tracer.withSpan(span.start())) {
      Message<Object> kafkaMessage = Methods.createKafkaMessage(message, topic);
      this.kafkaTemplate.send(kafkaMessage);

      span.tag(Constants.SPAN_KEY_OPERATION_TYPE, message.getItemStatus().name());
      span.tag(Constants.SPAN_KEY_ID_ORDER_ITEMS, message.getIdOrderItems());
      span.tag(Constants.SPAN_KEY_BARCODE, message.getBarcode());
      span.event("Kafka message sent");
    } finally {
      span.end();
    }
  }
}

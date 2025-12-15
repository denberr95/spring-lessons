package com.personal.springlessons.service.items;

import java.util.ArrayList;
import java.util.List;
import com.personal.springlessons.model.dto.ItemDTO;
import com.personal.springlessons.model.dto.OrderItemsDTO;
import com.personal.springlessons.model.dto.response.KafkaMessageItemDTO;
import com.personal.springlessons.model.entity.items.OrderItemsEntity;
import com.personal.springlessons.model.lov.Channel;
import com.personal.springlessons.model.lov.ItemStatus;
import com.personal.springlessons.repository.items.IOrderItemsRepository;
import com.personal.springlessons.util.Constants;
import com.personal.springlessons.util.Methods;
import org.springframework.kafka.core.KafkaTemplate;
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

      this.notifyKafkaItems(message, Constants.TOPIC_ITEMS);
    });
  }

  @Transactional(readOnly = true)
  @NewSpan
  public List<OrderItemsDTO> getAll() {
    Span currentSpan = this.tracer.currentSpan();
    List<OrderItemsDTO> result = new ArrayList<>();
    List<OrderItemsEntity> ordersData = this.orderItemsRepository.findAll();
    currentSpan.tag(Constants.SPAN_KEY_TOTAL_ORDERS, String.valueOf(ordersData.size()))
        .event("Orders retrieved");

    ordersData.forEach(i -> {

      OrderItemsDTO orderItemsDTO = new OrderItemsDTO();
      orderItemsDTO.setId(i.getId().toString());
      List<ItemDTO> items = new ArrayList<>(i.getItems().size());

      i.getItems().forEach(x -> {

        ItemDTO item = new ItemDTO();
        item.setBarcode(x.getBarcode());
        item.setId(x.getId().toString());
        item.setName(x.getName());
        item.setPrice(x.getPrice());
        items.add(item);
      });

      orderItemsDTO.setItems(items);
      orderItemsDTO.setQuantity(items.size());
      result.add(orderItemsDTO);
    });
    return result;
  }

  private void notifyKafkaItems(KafkaMessageItemDTO message, String topic) {
    Span span = this.tracer.nextSpan().name("notifyKafkaItems");
    try (Tracer.SpanInScope ws = this.tracer.withSpan(span.start())) {
      this.kafkaTemplate.send(Methods.createKafkaMessage(message, topic));
      span.tag(Constants.SPAN_KEY_OPERATION_TYPE, message.getItemStatus().name());
      span.tag(Constants.SPAN_KEY_ID_ORDER_ITEMS, message.getIdOrderItems());
      span.tag(Constants.SPAN_KEY_BARCODE, message.getBarcode());
      span.event("Kafka message sent");
    } finally {
      span.end();
    }
  }
}

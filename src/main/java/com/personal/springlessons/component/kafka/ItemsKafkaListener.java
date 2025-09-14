package com.personal.springlessons.component.kafka;

import com.personal.springlessons.component.mapper.IItemsMapper;
import com.personal.springlessons.exception.items.DuplicatedBarcodeException;
import com.personal.springlessons.model.csv.DiscardedItemCsv;
import com.personal.springlessons.model.dto.response.KafkaMessageItemDTO;
import com.personal.springlessons.model.entity.items.ItemsEntity;
import com.personal.springlessons.model.lov.ItemStatus;
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

@Component
public class ItemsKafkaListener {

    private static final Logger log = LoggerFactory.getLogger(ItemsKafkaListener.class);
    private final ApplicationEventPublisher applicationEventPublisher;
    private final IItemsRepository itemRepository;
    private final IOrderItemsRepository orderItemsRepository;
    private final IItemsMapper itemMapper;

    public ItemsKafkaListener(ApplicationEventPublisher applicationEventPublisher,
            IItemsRepository itemRepository, IOrderItemsRepository orderItemsRepository,
            IItemsMapper itemMapper) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.itemRepository = itemRepository;
        this.orderItemsRepository = orderItemsRepository;
        this.itemMapper = itemMapper;
    }

    @RetryableTopic(attempts = Constants.S_VAL_1, dltStrategy = DltStrategy.NO_DLT)
    @KafkaListener(groupId = "upload-items.group", topics = Constants.TOPIC_ITEMS,
            filter = "uploadItemsRecordFilter", concurrency = Constants.S_VAL_3)
    public void upload(@Payload KafkaMessageItemDTO message) {
        log.info("Received item to upload: '{}'", message);

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

            this.orderItemsRepository.updateStatusById(ItemStatus.DISCARDED,
                    Methods.idValidation(message.getIdOrderItems()));
            throw new DuplicatedBarcodeException(message.getBarcode(), item.getId().toString());
        });
        ItemsEntity data = this.itemMapper.mapMessageToEntity(message);
        this.itemRepository.saveAndFlush(data);
    }

    @RetryableTopic(attempts = Constants.S_VAL_1, dltStrategy = DltStrategy.NO_DLT)
    @KafkaListener(groupId = "delete-items.group", topics = Constants.TOPIC_ITEMS,
            filter = "deleteItemsRecordFilter", concurrency = Constants.S_VAL_3)
    public void delete(KafkaMessageItemDTO message) {
        log.info("Received item to delete: '{}'", message);
        this.itemRepository.deleteById(Methods.idValidation(message.getId()));
    }
}

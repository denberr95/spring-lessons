package com.personal.springlessons.component.kafka;

import com.personal.springlessons.component.mapper.IItemsMapper;
import com.personal.springlessons.exception.DuplicatedBarcodeException;
import com.personal.springlessons.model.csv.DiscardedItemCsv;
import com.personal.springlessons.model.dto.KafkaMessageItemDTO;
import com.personal.springlessons.model.entity.ItemsEntity;
import com.personal.springlessons.repository.IItemsRepository;
import com.personal.springlessons.util.Constants;
import com.personal.springlessons.util.Methods;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ItemsListener {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final IItemsRepository itemRepository;
    private final IItemsMapper itemMapper;

    @RetryableTopic(attempts = Constants.S_VAL_1, dltStrategy = DltStrategy.NO_DLT)
    @KafkaListener(groupId = "upload-items.group", topics = Constants.TOPIC_ITEMS,
            filter = "uploadItemsRecordFilter", concurrency = Constants.S_VAL_3)
    public void upload(KafkaMessageItemDTO message) {
        log.info("Received item to upload: '{}'", message.toString());
        this.itemRepository.findByBarcode(message.getBarcode()).ifPresent(item -> {
            log.warn("Barcode: '{}' already exists", item.getBarcode());
            DiscardedItemCsv event = new DiscardedItemCsv();
            event.setBarcode(item.getBarcode());
            event.setIdItem(item.getId().toString());
            event.setIdOrderItems(message.getIdOrderItems());
            event.setIdOrderItemsOriginal(item.getItemsStatusEntity().getId().toString());
            event.setName(message.getName());
            event.setPrice(message.getPrice());
            this.applicationEventPublisher.publishEvent(event);
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

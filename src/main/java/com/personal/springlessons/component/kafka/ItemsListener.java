package com.personal.springlessons.component.kafka;

import com.personal.springlessons.component.mapper.IItemsMapper;
import com.personal.springlessons.exception.DuplicatedBarcodeException;
import com.personal.springlessons.model.dto.KafkaMessageItemDTO;
import com.personal.springlessons.model.entity.ItemsEntity;
import com.personal.springlessons.repository.IItemsRepository;
import com.personal.springlessons.util.Constants;
import com.personal.springlessons.util.Methods;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ItemsListener {

    private final IItemsRepository itemRepository;
    private final IItemsMapper itemMapper;

    @KafkaListener(groupId = "upload-items.group", topics = Constants.TOPIC_ITEMS,
            filter = "uploadItemsRecordFilter", concurrency = Constants.S_VAL_3)
    public void upload(KafkaMessageItemDTO message) {
        log.info("Received item to upload: '{}'", message.toString());

        this.itemRepository.findByBarcode(message.getBarcode()).ifPresent(
                x -> new DuplicatedBarcodeException(message.getBarcode(), x.getId().toString()));

        ItemsEntity data = this.itemMapper.mapMessageToEntity(message);
        this.itemRepository.saveAndFlush(data);
    }

    @KafkaListener(groupId = "delete-items.group", topics = Constants.TOPIC_ITEMS,
            filter = "deleteItemsRecordFilter", concurrency = Constants.S_VAL_3)
    public void delete(KafkaMessageItemDTO message) {
        log.info("Received item to delete: '{}'", message);
        this.itemRepository.deleteById(Methods.idValidation(message.getId()));
    }
}

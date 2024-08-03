package com.personal.springlessons.component.kafka;

import com.personal.springlessons.component.mapper.IItemMapper;
import com.personal.springlessons.model.dto.KafkaMessagetItemDTO;
import com.personal.springlessons.repository.IItemRepository;
import com.personal.springlessons.repository.IItemStatusRepository;
import com.personal.springlessons.util.Constants;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ItemsListener {

    private final IItemRepository itemRepository;
    private final IItemStatusRepository itemStatusRepository;
    private final IItemMapper itemMapper;

    @KafkaListener(groupId = "upload-items.group", topics = Constants.TOPIC_ITEMS,
            filter = "uploadItemsFilter", concurrency = Constants.S_VAL_3)
    public void upload(KafkaMessagetItemDTO message) {
        log.info("Received item to upload: '{}'", message.toString());
        this.itemRepository
                .saveAndFlush(this.itemMapper.mapEntity(this.itemMapper.mapMessage(message)));
    }

    @KafkaListener(groupId = "delete-items.group", topics = Constants.TOPIC_ITEMS,
            filter = "deleteItemsFilter", concurrency = Constants.S_VAL_3)
    public void delete(KafkaMessagetItemDTO message) {
        log.info("Received item to delete: '{}'", message);
    }

    @KafkaListener(groupId = "update-items.group", topics = Constants.TOPIC_ITEMS,
            filter = "updateItemsFilter", concurrency = Constants.S_VAL_3)
    public void update(KafkaMessagetItemDTO message) {
        log.info("Received item to update: '{}'", message);
    }
}

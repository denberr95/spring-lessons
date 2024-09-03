package com.personal.springlessons.component.event;

import java.io.FileWriter;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.personal.springlessons.component.mapper.IItemsMapper;
import com.personal.springlessons.config.AppPropertiesConfig;
import com.personal.springlessons.model.csv.DiscardedItemCsv;
import com.personal.springlessons.model.dto.KafkaMessageItemDTO;
import com.personal.springlessons.util.Constants;
import com.personal.springlessons.util.Methods;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class DuplicatedBarcodeEvent {

    private final IItemsMapper itemsMapper;
    private final AppPropertiesConfig appPropertiesConfig;

    @EventListener
    public void duplicatedBarcodeHandler(KafkaMessageItemDTO kafkaMessageItemDTO) {
        log.info("Received event for duplicated barcode: '{}'", kafkaMessageItemDTO.getBarcode());

        DiscardedItemCsv discardedItem = this.itemsMapper.mapCsv(kafkaMessageItemDTO);
        String file = this.getFullFileName(discardedItem);

        try (FileWriter writer = new FileWriter(file);) {
            ColumnPositionMappingStrategy<DiscardedItemCsv> strategy =
                    new ColumnPositionMappingStrategy<>();
            strategy.setType(DiscardedItemCsv.class);
            StatefulBeanToCsv<DiscardedItemCsv> beanToCsv =
                    new StatefulBeanToCsvBuilder<DiscardedItemCsv>(writer)
                            .withMappingStrategy(strategy).build();
            beanToCsv.write(List.of(discardedItem));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        log.info("Event handled and wrote csv");
    }

    private String getFullFileName(DiscardedItemCsv discardedItem) {
        String tms = Methods.dateTimeFormatter(Constants.DATE_TIME_FORMAT_1, LocalDateTime.now());
        String fileName = discardedItem.getIdOrderItems() + Constants.UNDERSCORE
                + discardedItem.getIdItem() + Constants.UNDERSCORE + tms + Constants.CSV_EXT;
        return Paths.get(this.appPropertiesConfig.getCsvDir(), fileName).toString();
    }
}

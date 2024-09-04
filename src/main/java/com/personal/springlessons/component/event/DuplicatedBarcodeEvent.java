package com.personal.springlessons.component.event;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.personal.springlessons.config.AppPropertiesConfig;
import com.personal.springlessons.model.csv.DiscardedItemCsv;
import com.personal.springlessons.util.Constants;
import com.personal.springlessons.util.Methods;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class DuplicatedBarcodeEvent {

    private final AppPropertiesConfig appPropertiesConfig;

    @Async
    @EventListener
    public void duplicatedBarcodeHandler(DiscardedItemCsv discardedItemCsv) throws Exception {
        log.info("Received event for duplicated barcode: '{}'", discardedItemCsv.getBarcode());
        String file = this.createFile(discardedItemCsv);
        try (Writer writer = new FileWriter(file)) {
            HeaderColumnNameMappingStrategy<DiscardedItemCsv> strategy =
                    new HeaderColumnNameMappingStrategy<>();
            strategy.setType(DiscardedItemCsv.class);
            StatefulBeanToCsv<DiscardedItemCsv> beanToCsv =
                    new StatefulBeanToCsvBuilder<DiscardedItemCsv>(writer)
                            .withSeparator(this.appPropertiesConfig.getCsvColumnSeparator())
                            .withQuotechar(this.appPropertiesConfig.getQuoteCharacter())
                            .withMappingStrategy(strategy).build();
            beanToCsv.write(List.of(discardedItemCsv));
        }
        log.info("Event handled and wrote csv: '{}'", file);
    }

    private String createFile(DiscardedItemCsv discardedItemCsv) throws IOException {
        String tms = Methods.dateTimeFormatter(Constants.DATE_TIME_FORMAT_1, LocalDateTime.now());
        String fileName = discardedItemCsv.getIdOrderItems() + Constants.UNDERSCORE
                + discardedItemCsv.getBarcode() + Constants.UNDERSCORE + tms + Constants.CSV_EXT;
        Path filePath = Paths.get(this.appPropertiesConfig.getCsvDir(), fileName);
        Files.createDirectories(filePath.getParent());
        return Files.createFile(filePath).toString();
    }
}

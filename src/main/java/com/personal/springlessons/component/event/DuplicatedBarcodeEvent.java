package com.personal.springlessons.component.event;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.personal.springlessons.config.AppPropertiesConfig;
import com.personal.springlessons.model.csv.DiscardedItemCsv;
import com.personal.springlessons.util.Constants;
import com.personal.springlessons.util.Methods;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DuplicatedBarcodeEvent {

    private static final Logger log = LoggerFactory.getLogger(DuplicatedBarcodeEvent.class);
    private final AppPropertiesConfig appPropertiesConfig;

    @Async
    @EventListener
    public void duplicatedBarcodeHandler(DiscardedItemCsv discardedItemCsv)
            throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        log.info("Received event for duplicated barcode: '{}'", discardedItemCsv.getBarcode());
        String baseName = discardedItemCsv.getIdOrderItems() + Constants.C_UNDERSCORE
                + discardedItemCsv.getBarcode();
        String file = Methods.createFile(this.appPropertiesConfig.getCsvMetadata().getCsvDir(),
                baseName, Constants.CSV_EXT, true);
        try (Writer writer = new FileWriter(file)) {
            StatefulBeanToCsv<DiscardedItemCsv> beanToCsv =
                    new StatefulBeanToCsvBuilder<DiscardedItemCsv>(writer).withOrderedResults(true)
                            .withApplyQuotesToAll(
                                    this.appPropertiesConfig.getCsvMetadata().getApplyAllQuotes())
                            .withLineEnd(System.lineSeparator())
                            .withSeparator(
                                    this.appPropertiesConfig.getCsvMetadata().getColumnSeparator())
                            .withQuotechar(
                                    this.appPropertiesConfig.getCsvMetadata().getQuoteCharacter())
                            .build();
            beanToCsv.write(List.of(discardedItemCsv));
        }
        log.info("Event handled and wrote csv: '{}'", file);
    }
}

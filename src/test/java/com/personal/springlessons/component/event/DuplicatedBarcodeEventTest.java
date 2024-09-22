package com.personal.springlessons.component.event;

import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import com.personal.springlessons.config.AppPropertiesConfig;
import com.personal.springlessons.model.csv.DiscardedItemCsv;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;

@SpringBootTest
class DuplicatedBarcodeEventTest {

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private AppPropertiesConfig appPropertiesConfig;

    private DiscardedItemCsv data;

    @BeforeEach
    void initAll() {
        this.data = new DiscardedItemCsv();
        this.data.setIdOrderItems("id-order-item");
        this.data.setIdOrderItemsOriginal("id-order-items-original");
        this.data.setPrice(new BigDecimal(9_999));
        this.data.setIdItem("id-item");
        this.data.setBarcode("barcode");
        this.data.setName("name");
    }

    @AfterEach
    void tearDown() throws IOException {
        List<Path> list = Files.list(Paths.get(this.appPropertiesConfig.getCsvDir()))
                .collect(Collectors.toList());
        for (Path p : list) {
            Files.delete(p);
        }
    }

    @Test
    void givenEvent_whenDuplicatedBarcodeEvent_thenCreateFile() throws IOException {
        this.eventPublisher.publishEvent(this.data);
        String baseName = this.data.getIdOrderItems() + "_" + this.data.getBarcode() + "_";
        Path path = Paths.get(this.appPropertiesConfig.getCsvDir());
        Files.list(path).forEach(i -> {
            String actual = i.getFileName().toString();
            assertTrue(actual.startsWith(baseName));
            assertTrue(actual.endsWith(".csv"));
        });
    }
}

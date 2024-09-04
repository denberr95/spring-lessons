package com.personal.springlessons.model.csv;

import java.math.BigDecimal;
import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiscardedItemCsv {

    @CsvBindByName
    private String idOrderItems;

    @CsvBindByName
    private String idOrderItemsOriginal;

    @CsvBindByName
    private String idItem;

    @CsvBindByName
    private BigDecimal price;

    @CsvBindByName
    private String name;

    @CsvBindByName
    private String barcode;
}

package com.personal.springlessons.model.csv;

import java.math.BigDecimal;
import com.opencsv.bean.CsvBindByName;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DiscardedItem {

    @CsvBindByName
    private String idOrderItems;

    @CsvBindByName
    private String idItem;

    @CsvBindByName
    private BigDecimal price;

    @CsvBindByName
    private String name;

    @CsvBindByName
    private String barcode;
}

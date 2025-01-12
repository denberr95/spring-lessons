package com.personal.springlessons.model.csv;

import java.time.LocalDate;
import com.opencsv.bean.CsvBindByName;
import com.personal.springlessons.model.lov.Genre;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookCsv {

    @CsvBindByName
    private String name;

    @CsvBindByName
    private Integer numberOfPages;

    @CsvBindByName
    private LocalDate publicationDate;

    @CsvBindByName
    private Genre genre;
}

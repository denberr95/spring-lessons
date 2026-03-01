package com.personal.springlessons.model.csv;

import com.opencsv.bean.CsvBindByName;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BookCsv {

  @CsvBindByName(column = "name")
  private String name;

  @CsvBindByName(column = "number_of_pages")
  private String numberOfPages;

  @CsvBindByName(column = "publication_date")
  private String publicationDate;

  @CsvBindByName(column = "genre")
  private String genre;
}

package com.personal.springlessons.model.csv;

import java.util.Objects;

import com.opencsv.bean.CsvBindByName;

public class BookCsv {

  @CsvBindByName(column = "name")
  private String name;

  @CsvBindByName(column = "number_of_pages")
  private String numberOfPages;

  @CsvBindByName(column = "publication_date")
  private String publicationDate;

  @CsvBindByName(column = "genre")
  private String genre;

  public BookCsv() {
    // Required by opencsv for reflection-based bean instantiation
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getNumberOfPages() {
    return this.numberOfPages;
  }

  public void setNumberOfPages(String numberOfPages) {
    this.numberOfPages = numberOfPages;
  }

  public String getPublicationDate() {
    return this.publicationDate;
  }

  public void setPublicationDate(String publicationDate) {
    this.publicationDate = publicationDate;
  }

  public String getGenre() {
    return this.genre;
  }

  public void setGenre(String genre) {
    this.genre = genre;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof BookCsv that))
      return false;
    return Objects.equals(this.name, that.name)
        && Objects.equals(this.numberOfPages, that.numberOfPages)
        && Objects.equals(this.publicationDate, that.publicationDate)
        && Objects.equals(this.genre, that.genre);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.name, this.numberOfPages, this.publicationDate, this.genre);
  }

  @Override
  public String toString() {
    return "BookCsv{" + "name='" + this.name + '\'' + ", numberOfPages='" + this.numberOfPages
        + '\'' + ", publicationDate='" + this.publicationDate + '\'' + ", genre='" + this.genre
        + '\'' + '}';
  }
}

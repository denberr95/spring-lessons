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

  public BookCsv() {}

  public BookCsv(String name, String numberOfPages, String publicationDate, String genre) {
    this.name = name;
    this.numberOfPages = numberOfPages;
    this.publicationDate = publicationDate;
    this.genre = genre;
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
  public int hashCode() {
    return Objects.hash(this.name, this.numberOfPages, this.publicationDate, this.genre);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (this.getClass() != obj.getClass())
      return false;
    BookCsv other = (BookCsv) obj;
    return Objects.equals(this.name, other.name)
        && Objects.equals(this.numberOfPages, other.numberOfPages)
        && Objects.equals(this.publicationDate, other.publicationDate)
        && Objects.equals(this.genre, other.genre);
  }

  @Override
  public String toString() {
    return "BookCsv [name=" + this.name + ", numberOfPages=" + this.numberOfPages
        + ", publicationDate=" + this.publicationDate + ", genre=" + this.genre + "]";
  }
}

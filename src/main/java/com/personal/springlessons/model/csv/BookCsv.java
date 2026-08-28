package com.personal.springlessons.model.csv;

import java.util.Objects;

import com.opencsv.bean.CsvBindByName;

import org.jspecify.annotations.Nullable;

public class BookCsv {

  @CsvBindByName(column = "name")
  private @Nullable String name;

  @CsvBindByName(column = "number_of_pages")
  private @Nullable String numberOfPages;

  @CsvBindByName(column = "publication_date")
  private @Nullable String publicationDate;

  @CsvBindByName(column = "genre")
  private @Nullable String genre;

  public BookCsv() {
    // Required by opencsv for reflection-based bean instantiation
  }

  public @Nullable String getName() {
    return this.name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }

  public @Nullable String getNumberOfPages() {
    return this.numberOfPages;
  }

  public void setNumberOfPages(@Nullable String numberOfPages) {
    this.numberOfPages = numberOfPages;
  }

  public @Nullable String getPublicationDate() {
    return this.publicationDate;
  }

  public void setPublicationDate(@Nullable String publicationDate) {
    this.publicationDate = publicationDate;
  }

  public @Nullable String getGenre() {
    return this.genre;
  }

  public void setGenre(@Nullable String genre) {
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

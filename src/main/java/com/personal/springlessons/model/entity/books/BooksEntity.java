package com.personal.springlessons.model.entity.books;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;

import com.personal.springlessons.model.lov.Channel;
import com.personal.springlessons.model.lov.Genre;
import com.personal.springlessons.util.Constants;

import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SourceType;
import org.hibernate.generator.EventType;

@Entity
@DynamicInsert
@DynamicUpdate
@EntityListeners(value = BooksEntityListener.class)
@Table(name = BooksEntity.TABLE_NAME, schema = Constants.DB_SCHEMA_SPRING_APP, uniqueConstraints = {
    @UniqueConstraint(columnNames = {"name", "publication_date", "number_of_pages"})})
public class BooksEntity {

  public static final String TABLE_NAME = "books";

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;

  @CurrentTimestamp(source = SourceType.DB, event = EventType.INSERT)
  @Column(name = "created_at", nullable = false, updatable = false)
  private OffsetDateTime createdAt;

  @CurrentTimestamp(source = SourceType.VM, event = EventType.UPDATE)
  @Column(name = "updated_at")
  private OffsetDateTime updatedAt;

  @Version
  @Column(name = "version")
  private Long version;

  @Column(name = "name", nullable = false, length = Constants.I_VAL_100)
  private String name;

  @Column(name = "publication_date", nullable = false)
  private LocalDate publicationDate;

  @Column(name = "number_of_pages", nullable = false)
  private Integer numberOfPages;

  @Enumerated(EnumType.STRING)
  @Column(name = "channel", nullable = false)
  private Channel channel;

  @Enumerated(EnumType.STRING)
  @Column(name = "genre", nullable = false)
  private Genre genre;

  public BooksEntity(UUID id, OffsetDateTime createdAt, OffsetDateTime updatedAt, Long version,
      String name, LocalDate publicationDate, Integer numberOfPages, Channel channel, Genre genre) {
    this.id = id;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.version = version;
    this.name = name;
    this.publicationDate = publicationDate;
    this.numberOfPages = numberOfPages;
    this.channel = channel;
    this.genre = genre;
  }

  public BooksEntity() {}

  public static String getTableName() {
    return TABLE_NAME;
  }

  public UUID getId() {
    return this.id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public OffsetDateTime getCreatedAt() {
    return this.createdAt;
  }

  public void setCreatedAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public OffsetDateTime getUpdatedAt() {
    return this.updatedAt;
  }

  public void setUpdatedAt(OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public Long getVersion() {
    return this.version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public LocalDate getPublicationDate() {
    return this.publicationDate;
  }

  public void setPublicationDate(LocalDate publicationDate) {
    this.publicationDate = publicationDate;
  }

  public Integer getNumberOfPages() {
    return this.numberOfPages;
  }

  public void setNumberOfPages(Integer numberOfPages) {
    this.numberOfPages = numberOfPages;
  }

  public Channel getChannel() {
    return this.channel;
  }

  public void setChannel(Channel channel) {
    this.channel = channel;
  }

  public Genre getGenre() {
    return this.genre;
  }

  public void setGenre(Genre genre) {
    this.genre = genre;
  }
}

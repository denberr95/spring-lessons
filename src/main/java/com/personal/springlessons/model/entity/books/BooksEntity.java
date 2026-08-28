package com.personal.springlessons.model.entity.books;

import java.time.Instant;
import java.time.LocalDate;
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
import org.hibernate.envers.Audited;
import org.hibernate.generator.EventType;
import org.jspecify.annotations.Nullable;

@Entity
@Audited
@DynamicInsert
@DynamicUpdate
@EntityListeners(value = BooksEntityListener.class)
@Table(name = BooksEntity.TABLE_NAME, schema = Constants.DB_SCHEMA_SPRING_APP,
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name", "publication_date", "number_of_pages"},
            name = "uk_books_name_pub_date_pages")})
public class BooksEntity {

  protected static final String TABLE_NAME = "books";

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", updatable = false, nullable = false)
  private @Nullable UUID id;

  @CurrentTimestamp(source = SourceType.DB, event = EventType.INSERT)
  @Column(name = "created_at", nullable = false, updatable = false)
  private @Nullable Instant createdAt;

  @CurrentTimestamp(source = SourceType.DB, event = EventType.UPDATE)
  @Column(name = "updated_at")
  private @Nullable Instant updatedAt;

  @Version
  @Column(name = "version", nullable = false)
  private @Nullable Long version;

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

  @SuppressWarnings("java:S2637")
  public BooksEntity() {
    // Required by JPA
  }

  public @Nullable UUID getId() {
    return this.id;
  }

  public void setId(@Nullable UUID id) {
    this.id = id;
  }

  public @Nullable Instant getCreatedAt() {
    return this.createdAt;
  }

  public void setCreatedAt(@Nullable Instant createdAt) {
    this.createdAt = createdAt;
  }

  public @Nullable Instant getUpdatedAt() {
    return this.updatedAt;
  }

  public void setUpdatedAt(@Nullable Instant updatedAt) {
    this.updatedAt = updatedAt;
  }

  public @Nullable Long getVersion() {
    return this.version;
  }

  public void setVersion(@Nullable Long version) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof BooksEntity that))
      return false;
    return this.id != null && this.id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }

  @Override
  public String toString() {
    return "BooksEntity{" + "id=" + this.id + ", createdAt=" + this.createdAt + ", updatedAt="
        + this.updatedAt + ", version=" + this.version + ", name='" + this.name + '\''
        + ", publicationDate=" + this.publicationDate + ", numberOfPages=" + this.numberOfPages
        + ", channel=" + this.channel + ", genre=" + this.genre + '}';
  }
}

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
import com.personal.springlessons.model.lov.Channel;
import com.personal.springlessons.model.lov.Genre;
import com.personal.springlessons.util.Constants;
import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SourceType;
import org.hibernate.generator.EventType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
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
}

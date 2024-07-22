package com.personal.springlessons.model.entity;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import com.personal.springlessons.util.Constants;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@EntityListeners(value = BookEntityListener.class)
@Table(name = BookEntity.TABLE_NAME, schema = Constants.DB_SCHEMA_SPRING_APP, uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name", "publication_date", "number_of_pages"})})
public class BookEntity {

    public static final String TABLE_NAME = "books";

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "name", nullable = false, length = Constants.LEN_100)
    private String name;

    @Column(name = "publication_date", nullable = false)
    private LocalDate publicationDate;

    @Column(name = "number_of_pages", nullable = false)
    private Integer numberOfPages;
}

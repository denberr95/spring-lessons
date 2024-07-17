package com.personal.springlessons.repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import com.personal.springlessons.model.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IBookRepository extends JpaRepository<BookEntity, UUID> {

    Optional<BookEntity> findByNameAndPublicationDate(String name, LocalDate publicationDate);
}

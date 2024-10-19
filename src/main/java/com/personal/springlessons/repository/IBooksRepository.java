package com.personal.springlessons.repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import com.personal.springlessons.model.entity.books.BooksEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IBooksRepository extends JpaRepository<BooksEntity, UUID> {

    Optional<BooksEntity> findByNameAndPublicationDateAndNumberOfPages(String name,
            LocalDate publicationDate, Integer numberOfPages);

}

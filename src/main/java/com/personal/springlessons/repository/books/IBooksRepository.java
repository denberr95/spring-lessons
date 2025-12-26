package com.personal.springlessons.repository.books;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import com.personal.springlessons.model.entity.books.BooksEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;

public interface IBooksRepository
    extends JpaRepository<BooksEntity, UUID>, RevisionRepository<BooksEntity, UUID, Long> {

  Optional<BooksEntity> findByNameAndPublicationDateAndNumberOfPages(String name,
      LocalDate publicationDate, Integer numberOfPages);
}

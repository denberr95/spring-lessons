package com.personal.springlessons.component.mapper;

import java.util.List;
import com.personal.springlessons.model.dto.BookDTO;
import com.personal.springlessons.model.entity.BooksEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(config = ICustomMapperConfig.class)
public interface IBooksMapper {

    IBooksMapper INSTANCE = Mappers.getMapper(IBooksMapper.class);

    BookDTO mapDTO(BooksEntity bookEntity);

    List<BookDTO> mapDTO(List<BooksEntity> bookEntity);

    BooksEntity mapEntity(BookDTO bookDTO);

    List<BooksEntity> mapEntity(List<BookDTO> bookDTO);

    BookDTO update(BooksEntity s, @MappingTarget BookDTO t);

    BooksEntity update(BookDTO s, @MappingTarget BooksEntity t);

}

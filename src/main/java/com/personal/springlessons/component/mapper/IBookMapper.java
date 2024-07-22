package com.personal.springlessons.component.mapper;

import java.util.List;
import com.personal.springlessons.model.dto.BookDTO;
import com.personal.springlessons.model.entity.BookEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(config = ICustomMapperConfig.class)
public interface IBookMapper {

    IBookMapper INSTANCE = Mappers.getMapper(IBookMapper.class);

    BookDTO mapDTO(BookEntity bookEntity);

    List<BookDTO> mapDTO(List<BookEntity> bookEntity);

    BookEntity mapEntity(BookDTO bookDTO);

    List<BookEntity> mapEntity(List<BookDTO> bookDTO);

    BookDTO update(BookEntity s, @MappingTarget BookDTO t);

    BookEntity update(BookDTO s, @MappingTarget BookEntity t);

}

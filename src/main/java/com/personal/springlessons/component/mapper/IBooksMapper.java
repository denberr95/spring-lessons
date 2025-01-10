package com.personal.springlessons.component.mapper;

import java.util.List;
import com.personal.springlessons.model.dto.BookDTO;
import com.personal.springlessons.model.entity.books.BooksEntity;
import com.personal.springlessons.model.lov.Channel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(config = ICustomMapperConfig.class)
public interface IBooksMapper {

    IBooksMapper INSTANCE = Mappers.getMapper(IBooksMapper.class);

    BookDTO mapDTO(BooksEntity bookEntity);

    List<BookDTO> mapDTO(List<BooksEntity> bookEntity);

    BooksEntity mapEntity(BookDTO bookDTO, Channel channel);

    @Mapping(target = "channel", source = "channel")
    BooksEntity update(BookDTO source, Channel channel, @MappingTarget BooksEntity target);

}

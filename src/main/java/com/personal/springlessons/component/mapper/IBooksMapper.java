package com.personal.springlessons.component.mapper;

import java.util.List;
import com.personal.springlessons.model.csv.BookCsv;
import com.personal.springlessons.model.dto.BookDTO;
import com.personal.springlessons.model.entity.books.BooksEntity;
import com.personal.springlessons.model.lov.Channel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(config = ICustomMapperConfig.class, uses = MappingUtils.class)
public interface IBooksMapper {

    IBooksMapper INSTANCE = Mappers.getMapper(IBooksMapper.class);

    BooksEntity mapEntity(BookDTO source, Channel channel);

    BookDTO mapDTO(BooksEntity source);

    List<BookDTO> mapDTO(List<BooksEntity> source);

    @Mapping(source = "channel", target = "channel")
    BooksEntity update(BookDTO source, Channel channel, @MappingTarget BooksEntity target, String version);

    List<BookCsv> mapCsv(List<BooksEntity> source);

    @Mapping(target = "publicationDate", qualifiedByName = "stringToLocalDate")
    @Mapping(target = "numberOfPages", qualifiedByName = "stringToInteger")
    @Mapping(target = "genre", qualifiedByName = "stringToGenre")
    BookDTO mapDTO(BookCsv source);
}

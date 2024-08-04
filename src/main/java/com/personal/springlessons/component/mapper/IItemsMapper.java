package com.personal.springlessons.component.mapper;

import com.personal.springlessons.model.dto.ItemDTO;
import com.personal.springlessons.model.dto.KafkaMessageItemDTO;
import com.personal.springlessons.model.entity.ItemsEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(config = ICustomMapperConfig.class)
public interface IItemsMapper {

    IItemsMapper INSTANCE = Mappers.getMapper(IItemsMapper.class);

    KafkaMessageItemDTO mapMessage(ItemDTO itemDTO);

    ItemsEntity mapMessageToEntity(KafkaMessageItemDTO kafkaMessageItemDTO);

}

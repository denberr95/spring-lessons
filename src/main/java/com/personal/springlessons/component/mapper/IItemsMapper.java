package com.personal.springlessons.component.mapper;

import java.util.List;
import com.personal.springlessons.model.dto.ItemDTO;
import com.personal.springlessons.model.dto.response.KafkaMessageItemDTO;
import com.personal.springlessons.model.entity.items.ItemsEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(config = ICustomMapperConfig.class)
public interface IItemsMapper {

    IItemsMapper INSTANCE = Mappers.getMapper(IItemsMapper.class);

    KafkaMessageItemDTO mapMessage(ItemDTO itemDTO);

    List<ItemDTO> mapDTO(List<ItemsEntity> itemsEntities);

    @Mapping(source = "idOrderItems", target = "orderItemsEntity.id")
    ItemsEntity mapMessageToEntity(KafkaMessageItemDTO kafkaMessageItemDTO);
    
}

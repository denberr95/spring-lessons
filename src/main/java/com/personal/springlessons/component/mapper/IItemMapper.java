package com.personal.springlessons.component.mapper;

import java.util.List;
import com.personal.springlessons.model.dto.ItemDTO;
import com.personal.springlessons.model.dto.KafkaMessagetItemDTO;
import com.personal.springlessons.model.entity.ItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(config = ICustomMapperConfig.class)
public interface IItemMapper {

    IItemMapper INSTANCE = Mappers.getMapper(IItemMapper.class);

    ItemDTO mapDTO(ItemEntity itemEntity);

    List<ItemDTO> mapDTO(List<ItemEntity> itemEntity);

    ItemEntity mapEntity(ItemDTO itemDTO);

    List<ItemEntity> mapEntity(List<ItemDTO> itemDTO);

    KafkaMessagetItemDTO mapMessage(ItemDTO itemDTO);

    ItemDTO mapMessage(KafkaMessagetItemDTO kafkaMessagetItemDTO);
}

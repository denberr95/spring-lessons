package com.personal.springlessons.model.dto;

import com.personal.springlessons.model.lov.ItemStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class KafkaMessageItemDTO extends ItemDTO {

    private String idOrderItems;
    
    private ItemStatus itemStatus;
}

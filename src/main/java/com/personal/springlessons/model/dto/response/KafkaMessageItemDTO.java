package com.personal.springlessons.model.dto.response;

import com.personal.springlessons.model.dto.ItemDTO;
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

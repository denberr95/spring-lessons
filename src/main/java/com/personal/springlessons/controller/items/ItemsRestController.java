package com.personal.springlessons.controller.items;

import java.util.List;
import com.personal.springlessons.model.dto.ItemDTO;
import com.personal.springlessons.model.lov.Channel;
import com.personal.springlessons.service.ItemsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import io.micrometer.observation.annotation.Observed;

@RestController
public class ItemsRestController implements IItemsRestController {

    private static final Logger log = LoggerFactory.getLogger(ItemsRestController.class);
    private final ItemsService itemService;

    public ItemsRestController(ItemsService itemService) {
        this.itemService = itemService;
    }

    @Observed(name = "upload.items", contextualName = "items-upload")
    @PreAuthorize(value = "hasAuthority('SCOPE_items:upload')")
    @Override
    public ResponseEntity<Void> upload(final List<ItemDTO> items, final Channel channel) {
        log.info("Called API to upload items");
        this.itemService.upload(items, channel);
        log.info("Kafka message to upload items sent");
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Observed(name = "delete.items", contextualName = "items-delete",
            lowCardinalityKeyValues = {"endpoint", "/items/{id}"})
    @PreAuthorize(value = "hasAuthority('SCOPE_items:delete')")
    @Override
    public ResponseEntity<Void> delete(final List<ItemDTO> items, final Channel channel) {
        log.info("Called API to delete items");
        this.itemService.delete(items, channel);
        log.info("Kafka message to delete items sent");
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize(value = "hasAuthority('SCOPE_items:get')")
    @Override
    public ResponseEntity<List<ItemDTO>> getAll() {
        log.info("Called API to retrieve all items");
        List<ItemDTO> result = this.itemService.getAll();
        ResponseEntity<List<ItemDTO>> response;
        if (result.isEmpty()) {
            log.info("No items retrieved !");
            response = ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            log.info("Items retrieved !");
            response = ResponseEntity.status(HttpStatus.OK).body(result);
        }
        return response;
    }
}

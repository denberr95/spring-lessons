package com.personal.springlessons.controller.items;

import java.util.List;
import com.personal.springlessons.model.dto.ItemDTO;
import com.personal.springlessons.service.ItemsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.micrometer.tracing.annotation.NewSpan;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "Items")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "items")
public class ItemsRestController {

    private final ItemsService itemService;

    @NewSpan
    @PostMapping
    @PreAuthorize(value = "hasAuthority('SCOPE_items:upload')")
    public ResponseEntity<Void> upload(@RequestBody List<ItemDTO> items) {
        log.info("Called API to upload items");
        this.itemService.upload(items);
        log.info("Kafka message to upload items sent");
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @NewSpan
    @DeleteMapping
    @PreAuthorize(value = "hasAuthority('SCOPE_items:delete')")
    public ResponseEntity<Void> delete(@RequestBody List<ItemDTO> items) {
        log.info("Called API to delete items");
        this.itemService.delete(items);
        log.info("Kafka message to delete items sent");
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @NewSpan
    @GetMapping
    @PreAuthorize(value = "hasAuthority('SCOPE_items:get')")
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

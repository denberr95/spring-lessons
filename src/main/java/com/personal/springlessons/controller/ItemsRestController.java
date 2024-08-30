package com.personal.springlessons.controller;

import java.util.List;
import com.personal.springlessons.model.dto.ItemDTO;
import com.personal.springlessons.service.ItemsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.micrometer.tracing.annotation.NewSpan;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
}

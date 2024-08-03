package com.personal.springlessons.controller;

import java.util.List;
import com.personal.springlessons.model.dto.ItemDTO;
import com.personal.springlessons.service.ItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.micrometer.tracing.annotation.NewSpan;
import io.micrometer.tracing.annotation.SpanTag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "items")
public class ItemRestController {

    private final ItemService itemService;

    @NewSpan
    @PostMapping
    public ResponseEntity<Void> upload(@RequestBody List<ItemDTO> items) {
        this.itemService.upload(items);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @NewSpan
    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestBody List<ItemDTO> items) {
        this.itemService.delete(items);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @NewSpan
    @PutMapping(path = "{id}")
    public ResponseEntity<Void> update(@SpanTag @PathVariable String id,
            @RequestBody List<ItemDTO> items) {
        this.itemService.update(id, items);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

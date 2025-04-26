package com.personal.springlessons.controller.items;

import java.util.List;
import jakarta.validation.Valid;
import com.personal.springlessons.model.dto.ItemDTO;
import com.personal.springlessons.model.dto.response.GenericErrorResponseDTO;
import com.personal.springlessons.model.dto.response.InvalidArgumentTypeResponseDTO;
import com.personal.springlessons.model.dto.response.NotReadableBodyRequestResponseDTO;
import com.personal.springlessons.model.dto.response.ValidationRequestErrorResponseDTO;
import com.personal.springlessons.model.lov.Channel;
import com.personal.springlessons.service.ItemsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.micrometer.tracing.annotation.NewSpan;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Items")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "items")
public class ItemsRestController {

    private static final Logger log = LoggerFactory.getLogger(ItemsRestController.class);
    private final ItemsService itemService;

    @NewSpan
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize(value = "hasAuthority('SCOPE_items:upload')")
    @Operation(summary = "Upload items", operationId = "uploadItems")
    @ApiResponse(responseCode = "204", description = "No Content",
            content = {@Content(schema = @Schema(implementation = Void.class))})
    @ApiResponse(responseCode = "400", description = "Bad Request",
            content = {@Content(schema = @Schema(oneOf = {ValidationRequestErrorResponseDTO.class,
                    InvalidArgumentTypeResponseDTO.class,
                    NotReadableBodyRequestResponseDTO.class}))})
    @ApiResponse(responseCode = "500", description = "Internal Server Error",
            content = {@Content(schema = @Schema(implementation = GenericErrorResponseDTO.class))})
    public ResponseEntity<Void> upload(@Valid @RequestBody final List<ItemDTO> items,
            @RequestHeader final Channel channel) {
        log.info("Called API to upload items");
        this.itemService.upload(items, channel);
        log.info("Kafka message to upload items sent");
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @NewSpan
    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize(value = "hasAuthority('SCOPE_items:delete')")
    @Operation(summary = "Delete items", operationId = "deleteItems")
    @ApiResponse(responseCode = "204", description = "No Content",
            content = {@Content(schema = @Schema(implementation = Void.class))})
    @ApiResponse(responseCode = "400", description = "Bad Request",
            content = {@Content(schema = @Schema(oneOf = {ValidationRequestErrorResponseDTO.class,
                    InvalidArgumentTypeResponseDTO.class,
                    NotReadableBodyRequestResponseDTO.class}))})
    @ApiResponse(responseCode = "500", description = "Internal Server Error",
            content = {@Content(schema = @Schema(implementation = GenericErrorResponseDTO.class))})
    public ResponseEntity<Void> delete(@Valid @RequestBody final List<ItemDTO> items,
            @RequestHeader final Channel channel) {
        log.info("Called API to delete items");
        this.itemService.delete(items, channel);
        log.info("Kafka message to delete items sent");
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @NewSpan
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize(value = "hasAuthority('SCOPE_items:get')")
    @Operation(summary = "Get all items", operationId = "getAllItems")
    @ApiResponse(responseCode = "200", description = "OK",
            content = {@Content(
                    array = @ArraySchema(schema = @Schema(implementation = ItemDTO.class)))})
    @ApiResponse(responseCode = "204", description = "No Content",
            content = {@Content(schema = @Schema(implementation = Void.class))})
    @ApiResponse(responseCode = "500", description = "Internal Server Error",
            content = {@Content(schema = @Schema(implementation = GenericErrorResponseDTO.class))})
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

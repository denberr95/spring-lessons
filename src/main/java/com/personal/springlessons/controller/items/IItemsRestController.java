package com.personal.springlessons.controller.items;

import java.util.List;
import jakarta.validation.Valid;
import com.personal.springlessons.model.dto.ItemDTO;
import com.personal.springlessons.model.dto.response.GenericErrorResponseDTO;
import com.personal.springlessons.model.dto.response.InvalidArgumentTypeResponseDTO;
import com.personal.springlessons.model.dto.response.NotReadableBodyRequestResponseDTO;
import com.personal.springlessons.model.dto.response.ValidationRequestErrorResponseDTO;
import com.personal.springlessons.model.lov.Channel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Items")
@Tag(name = "V1")
@Validated
@RequestMapping(path = "/v1/items")
public interface IItemsRestController {

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Upload items", operationId = "uploadItemsV1")
    @ApiResponse(responseCode = "204", description = "No Content",
            content = {@Content(schema = @Schema(implementation = Void.class))})
    @ApiResponse(responseCode = "400", description = "Bad Request",
            content = {@Content(schema = @Schema(oneOf = {ValidationRequestErrorResponseDTO.class,
                    InvalidArgumentTypeResponseDTO.class,
                    NotReadableBodyRequestResponseDTO.class}))})
    @ApiResponse(responseCode = "500", description = "Internal Server Error",
            content = {@Content(schema = @Schema(implementation = GenericErrorResponseDTO.class))})
    ResponseEntity<Void> upload(@Valid @RequestBody final List<ItemDTO> items,
            @RequestHeader final Channel channel);

    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Delete items", operationId = "deleteItems")
    @ApiResponse(responseCode = "204", description = "No Content",
            content = {@Content(schema = @Schema(implementation = Void.class))})
    @ApiResponse(responseCode = "400", description = "Bad Request",
            content = {@Content(schema = @Schema(oneOf = {ValidationRequestErrorResponseDTO.class,
                    InvalidArgumentTypeResponseDTO.class,
                    NotReadableBodyRequestResponseDTO.class}))})
    @ApiResponse(responseCode = "500", description = "Internal Server Error",
            content = {@Content(schema = @Schema(implementation = GenericErrorResponseDTO.class))})
    ResponseEntity<Void> delete(@Valid @RequestBody final List<ItemDTO> items,
            @RequestHeader final Channel channel);

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get all items", operationId = "getAllItems")
    @ApiResponse(responseCode = "200", description = "OK",
            content = {@Content(
                    array = @ArraySchema(schema = @Schema(implementation = ItemDTO.class)))})
    @ApiResponse(responseCode = "204", description = "No Content",
            content = {@Content(schema = @Schema(implementation = Void.class))})
    @ApiResponse(responseCode = "500", description = "Internal Server Error",
            content = {@Content(schema = @Schema(implementation = GenericErrorResponseDTO.class))})
    ResponseEntity<List<ItemDTO>> getAll();
}

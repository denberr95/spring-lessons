package com.personal.springlessons.controller.items;

import com.personal.springlessons.model.dto.OrderItemsDTO;
import com.personal.springlessons.model.dto.wrapper.OrderItemsWrapperDTO;
import com.personal.springlessons.model.lov.Channel;
import com.personal.springlessons.service.items.ItemsService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
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

  @Observed(name = "items.upload", contextualName = "items-upload",
      lowCardinalityKeyValues = {"endpoint", "/items"})
  @PreAuthorize(value = "hasAuthority('SCOPE_items:upload')")
  @Override
  public ResponseEntity<OrderItemsDTO> upload(final OrderItemsDTO order, final Channel channel) {
    log.info("Called API to upload items");
    OrderItemsDTO result = this.itemService.upload(order, channel);
    log.info("Kafka message to upload items sent");
    return ResponseEntity.status(HttpStatus.OK).body(result);
  }

  @Observed(name = "items.delete", contextualName = "items-delete",
      lowCardinalityKeyValues = {"endpoint", "/items"})
  @PreAuthorize(value = "hasAuthority('SCOPE_items:delete')")
  @Override
  public ResponseEntity<Void> delete(final OrderItemsDTO order, final Channel channel) {
    log.info("Called API to delete items");
    this.itemService.delete(order, channel);
    log.info("Kafka message to delete items sent");
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @Observed(name = "items.get.all", contextualName = "items-get-all",
      lowCardinalityKeyValues = {"endpoint", "/items"})
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(value = "hasAuthority('SCOPE_items:get')")
  @Override
  public ResponseEntity<OrderItemsWrapperDTO> getAll(Pageable pageable) {
    log.info("Called API to retrieve all items");
    OrderItemsWrapperDTO result = this.itemService.getAll(pageable);
    ResponseEntity<OrderItemsWrapperDTO> response;
    if (result.getContent().isEmpty()) {
      log.info("No items retrieved !");
      response = ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    } else {
      log.info("Items retrieved !");
      response = ResponseEntity.status(HttpStatus.OK).body(result);
    }
    return response;
  }
}

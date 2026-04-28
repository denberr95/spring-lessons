package com.personal.springlessons.service.items;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ItemsServiceTest {

  @Test
  void givenOrderWithItems_whenUpload_thenOrderIsSavedAndKafkaMessagesAreSent() {
    // TODO
  }

  @Test
  void givenOrderWithItems_whenDelete_thenKafkaMessagesAreSent() {
    // TODO
  }

  @Test
  void givenOrderItems_whenGetAll_thenReturnPaginatedResult() {
    // TODO
  }

  @Test
  void givenEmptyDatabase_whenGetAll_thenReturnEmptyPage() {
    // TODO
  }
}

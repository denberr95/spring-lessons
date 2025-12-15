package com.personal.springlessons.repository.items;

import com.personal.springlessons.model.entity.items.OrderItemsEntity;
import com.personal.springlessons.model.lov.Channel;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class IOrderItemsRepositoryTest {

  @Autowired
  private IOrderItemsRepository orderItemsRepository;

  private static final int TOTAL = 5;

  @BeforeEach
  void init() {
    for (int i = 0; i < TOTAL; i++) {
      OrderItemsEntity entity = new OrderItemsEntity();
      entity.setChannel(Channel.NA);
      this.orderItemsRepository.save(entity);
    }
  }

  @AfterEach
  void tearDown() {
    this.orderItemsRepository.deleteAll();
  }

}

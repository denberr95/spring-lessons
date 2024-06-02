package com.personal.springlessons;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import com.personal.springlessons.controller.FileSystemController;
import com.personal.springlessons.service.FileSystemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class SpringLessonsApplicationTests {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void contextLoads() {
        assertNotNull(applicationContext.getBean(FileSystemController.class));
        assertNotNull(applicationContext.getBean(FileSystemService.class));
    }
}

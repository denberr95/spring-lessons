package com.personal.springlessons;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import com.personal.springlessons.component.CustomInfoContributor;
import com.personal.springlessons.config.AspectSpanConfig;
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
    void contextControllerLoad() {
        assertNotNull(applicationContext.getBean(FileSystemController.class));
    }

    @Test
    void contextServiceLoad() {
        assertNotNull(applicationContext.getBean(FileSystemService.class));
    }

    @Test
    void contextComponentLoad() {
        assertNotNull(applicationContext.getBean(CustomInfoContributor.class));
    }

    @Test
    void contextConfigLoad() {
        assertNotNull(applicationContext.getBean(AspectSpanConfig.class));
    }
}

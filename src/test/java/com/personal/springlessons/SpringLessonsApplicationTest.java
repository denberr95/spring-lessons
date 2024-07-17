package com.personal.springlessons;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import com.personal.springlessons.component.CustomInfoContributor;
import com.personal.springlessons.config.AppPropertiesConfig;
import com.personal.springlessons.config.AspectSpanConfig;
import com.personal.springlessons.config.OpenTelemetryConfig;
import com.personal.springlessons.controller.BookRestController;
import com.personal.springlessons.controller.BookRestControllerAdvice;
import com.personal.springlessons.repository.IBookRepository;
import com.personal.springlessons.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class SpringLessonsApplicationTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void contextControllerLoad() {
        assertNotNull(this.applicationContext.getBean(BookRestController.class));
        assertNotNull(this.applicationContext.getBean(BookRestControllerAdvice.class));
    }

    @Test
    void contextServiceLoad() {
        assertNotNull(this.applicationContext.getBean(BookService.class));
    }

    @Test
    void contextComponentLoad() {
        assertNotNull(this.applicationContext.getBean(CustomInfoContributor.class));
    }

    @Test
    void contextConfigLoad() {
        assertNotNull(this.applicationContext.getBean(AspectSpanConfig.class));
        assertNotNull(this.applicationContext.getBean(OpenTelemetryConfig.class));
        assertNotNull(this.applicationContext.getBean(AppPropertiesConfig.class));
    }

    @Test
    void contextRepositoryLoad() {
        assertNotNull(this.applicationContext.getBean(IBookRepository.class));
    }
}

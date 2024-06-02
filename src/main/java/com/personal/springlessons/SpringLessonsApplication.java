package com.personal.springlessons;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.boot.web.context.WebServerPortFileWriter;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class SpringLessonsApplication {

    public static void main(final String[] args) {
        final SpringApplication springApplication =
                new SpringApplication(SpringLessonsApplication.class);
        springApplication.addListeners(new ApplicationPidFileWriter(),
                new WebServerPortFileWriter());
        springApplication.run(args);
    }
}

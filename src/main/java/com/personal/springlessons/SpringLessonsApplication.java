package com.personal.springlessons;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.boot.web.server.context.WebServerPortFileWriter;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.envers.repository.support.EnversRevisionRepositoryFactoryBean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.ws.config.annotation.EnableWs;

@EnableWs
@EnableKafka
@EnableAsync
@EnableMethodSecurity
@SpringBootApplication
@EnableJpaRepositories(repositoryFactoryBeanClass = EnversRevisionRepositoryFactoryBean.class)
@EnableAspectJAutoProxy
public class SpringLessonsApplication {

  public static void main(final String[] args) {
    final SpringApplication springApplication =
        new SpringApplication(SpringLessonsApplication.class);
    springApplication.addListeners(new ApplicationPidFileWriter(), new WebServerPortFileWriter());
    springApplication.run(args);
  }
}

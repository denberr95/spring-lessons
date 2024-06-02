package com.personal.springlessons.config;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.annotation.DefaultNewSpanParser;
import io.micrometer.tracing.annotation.ImperativeMethodInvocationProcessor;
import io.micrometer.tracing.annotation.MethodInvocationProcessor;
import io.micrometer.tracing.annotation.NewSpanParser;
import io.micrometer.tracing.annotation.SpanAspect;

@Configuration
public class AspectSpanConfig {

    @Bean
    NewSpanParser newSpanParser() {
        return new DefaultNewSpanParser();
    }

    @Bean
    MethodInvocationProcessor methodInvocationProcessor(final NewSpanParser newSpanParser,
            final Tracer tracer, final BeanFactory beanFactory) {
        return new ImperativeMethodInvocationProcessor(newSpanParser, tracer, beanFactory::getBean,
                beanFactory::getBean);
    }

    @Bean
    SpanAspect spanAspect(final MethodInvocationProcessor methodInvocationProcessor) {
        return new SpanAspect(methodInvocationProcessor);
    }
}

package com.personal.springlessons.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.exporter.otlp.logs.OtlpGrpcLogRecordExporter;
import io.opentelemetry.instrumentation.logback.appender.v1_0.OpenTelemetryAppender;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.logs.LogRecordProcessor;
import io.opentelemetry.sdk.logs.SdkLoggerProvider;
import io.opentelemetry.sdk.logs.SdkLoggerProviderBuilder;
import io.opentelemetry.sdk.logs.export.BatchLogRecordProcessor;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.semconv.ServiceAttributes;

@Configuration(proxyBeanMethods = false)
public class OpenTelemetryConfig {

    @Value("${management.otlp.logging.endpoint}")
    private String otlpGrpcLogRecordExporter;

    @Value("${spring.application.name:application}")
    private String applicationName;

    @Bean
    OpenTelemetrySdk openTelemetry(final SdkLoggerProvider sdkLoggerProvider,
            final SdkTracerProvider sdkTracerProvider,
            final ContextPropagators contextPropagators) {
        final OpenTelemetrySdk openTelemetrySdk = OpenTelemetrySdk.builder()
                .setLoggerProvider(sdkLoggerProvider).setTracerProvider(sdkTracerProvider)
                .setPropagators(contextPropagators).build();
        OpenTelemetryAppender.install(openTelemetrySdk);
        return openTelemetrySdk;
    }

    @Bean
    SdkLoggerProvider otelSdkLoggerProvider(final Environment environment,
            final ObjectProvider<LogRecordProcessor> logRecordProcessors) {
        final SdkLoggerProviderBuilder builder =
                SdkLoggerProvider.builder().setResource(Resource.getDefault().toBuilder()
                        .put(ServiceAttributes.SERVICE_NAME, this.applicationName).build());
        logRecordProcessors.orderedStream().forEach(builder::addLogRecordProcessor);
        return builder.build();
    }

    @Bean
    BatchLogRecordProcessor otelLogRecordProcessor() {
        return BatchLogRecordProcessor.builder(OtlpGrpcLogRecordExporter.builder()
                .setEndpoint(this.otlpGrpcLogRecordExporter).build()).build();
    }
}

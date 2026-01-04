package com.personal.springlessons.config;

import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;
import com.personal.springlessons.component.interceptor.HttpClientInterceptor;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.core5.pool.PoolConcurrencyPolicy;
import org.apache.hc.core5.pool.PoolReusePolicy;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import io.micrometer.observation.ObservationRegistry;

@Configuration(proxyBeanMethods = false)
public class RestClientConfig {

  @Bean
  RestClient restClient(HttpClientInterceptor httpClientInterceptor,
      ClientHttpRequestFactory clientHttpRequestFactory, ObservationRegistry observationRegistry,
      AppPropertiesConfig appPropertiesConfig) {
    return RestClient.builder().requestFactory(clientHttpRequestFactory)
        .baseUrl(appPropertiesConfig.getApiClient().getBaseUrl())
        .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .defaultHeader(HttpHeaders.ACCEPT_CHARSET, Charset.defaultCharset().name())
        .observationRegistry(observationRegistry).requestInterceptor(httpClientInterceptor).build();
  }

  @Bean
  BufferingClientHttpRequestFactory clientHttpRequestFactory(
      CloseableHttpClient closeableHttpClient) {
    HttpComponentsClientHttpRequestFactory factory =
        new HttpComponentsClientHttpRequestFactory(closeableHttpClient);
    return new BufferingClientHttpRequestFactory(factory);
  }

  @Bean
  CloseableHttpClient closeableHttpClient(
      PoolingHttpClientConnectionManager poolingHttpClientConnectionManager,
      RequestConfig requestConfig) {
    return HttpClientBuilder.create().setConnectionManager(poolingHttpClientConnectionManager)
        .setDefaultRequestConfig(requestConfig).build();
  }

  @Bean
  PoolingHttpClientConnectionManager poolingHttpClientConnectionManager(
      AppPropertiesConfig appPropertiesConfig, ConnectionConfig connectionConfig) {
    return PoolingHttpClientConnectionManagerBuilder.create()
        .setPoolConcurrencyPolicy(PoolConcurrencyPolicy.STRICT)
        .setConnPoolPolicy(PoolReusePolicy.LIFO).setDefaultConnectionConfig(connectionConfig)
        .setMaxConnPerRoute(appPropertiesConfig.getApiClient().getMaxConnPerRoute())
        .setMaxConnTotal(appPropertiesConfig.getApiClient().getMaxConnTotal()).build();
  }

  @Bean
  ConnectionConfig connectionConfig(AppPropertiesConfig appPropertiesConfig) {
    return ConnectionConfig.custom()
        .setConnectTimeout(
            Timeout.ofSeconds(appPropertiesConfig.getApiClient().getConnectionTimeout()))
        .setSocketTimeout(Timeout.ofSeconds(appPropertiesConfig.getApiClient().getSocketTimeout()))
        .setTimeToLive(TimeValue.ofSeconds(appPropertiesConfig.getApiClient().getTimeToLive()))
        .build();
  }

  @Bean
  RequestConfig requestConfig(AppPropertiesConfig appPropertiesConfig) {
    return RequestConfig.custom()
        .setConnectionRequestTimeout(
            Timeout.ofSeconds(appPropertiesConfig.getApiClient().getConnectionRequestTimeout()))
        .setDefaultKeepAlive(appPropertiesConfig.getApiClient().getKeepAlive(), TimeUnit.SECONDS)
        .setMaxRedirects(appPropertiesConfig.getApiClient().getMaxRedirects()).build();
  }
}

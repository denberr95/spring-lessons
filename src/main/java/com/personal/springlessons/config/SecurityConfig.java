package com.personal.springlessons.config;

import com.personal.springlessons.component.access.CustomAccessDeniedHandler;
import com.personal.springlessons.component.access.CustomAuthenticationEntryPoint;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.DefaultSecurityFilterChain;

@Configuration(proxyBeanMethods = false)
public class SecurityConfig {

  @Bean
  DefaultSecurityFilterChain configure(HttpSecurity http,
      CustomAccessDeniedHandler customAccessDeniedHandler,
      CustomAuthenticationEntryPoint customAuthenticationEntryPoint) {
    http.exceptionHandling(exceptionHandling -> exceptionHandling
        .authenticationEntryPoint(customAuthenticationEntryPoint)
        .accessDeniedHandler(customAccessDeniedHandler))
        .csrf((Customizer<CsrfConfigurer<HttpSecurity>>) CsrfConfigurer::disable)
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .oauth2ResourceServer(
            oauth2 -> oauth2.authenticationEntryPoint(customAuthenticationEntryPoint)
                .accessDeniedHandler(customAccessDeniedHandler).jwt(Customizer.withDefaults()));
    return http.build();
  }

  @Bean
  DefaultAuthenticationEventPublisher authenticationEventPublisher(
      ApplicationEventPublisher applicationEventPublisher) {
    return new DefaultAuthenticationEventPublisher(applicationEventPublisher);
  }
}

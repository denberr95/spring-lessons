package com.personal.springlessons.config;

import java.util.List;

import com.personal.springlessons.util.Methods;

import org.springframework.boot.info.GitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.SpecVersion;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;

@Configuration(proxyBeanMethods = false)
public class OpenAPIConfig {

  @Bean
  OpenAPI openAPI(Info info, SecurityRequirement securityRequirement, Components components) {
    OpenAPI openAPI = new OpenAPI();
    openAPI.setInfo(info);
    openAPI.setSpecVersion(SpecVersion.V31);
    openAPI.setSecurity(List.of(securityRequirement));
    openAPI.setComponents(components);
    return openAPI;
  }

  @Bean
  Components components(SecurityScheme securityScheme) {
    Components components = new Components();
    components.addSecuritySchemes(securityScheme.getName(), securityScheme);
    return components;
  }

  @Bean
  SecurityRequirement securityRequirement(SecurityScheme securityScheme) {
    SecurityRequirement securityRequirement = new SecurityRequirement();
    securityRequirement.addList(securityScheme.getName());
    return securityRequirement;
  }

  @Bean
  Info info(GitProperties gitProperties) {
    Info info = new Info();
    info.setDescription("Open API Spring Lessons");
    info.setTitle("Spring Lessons");
    info.setVersion(Methods.getApplicationVersion(gitProperties));
    return info;
  }

  @Bean
  SecurityScheme securityScheme(OAuthFlows oAuthFlows) {
    SecurityScheme securityScheme = new SecurityScheme();
    securityScheme.setName("oauth2");
    securityScheme.setType(Type.OAUTH2);
    securityScheme.setIn(SecurityScheme.In.HEADER);
    securityScheme.setFlows(oAuthFlows);
    return securityScheme;
  }

  @Bean
  OAuthFlows oAuthFlows(OAuthFlow oAuthFlow) {
    OAuthFlows oAuthFlows = new OAuthFlows();
    oAuthFlows.setClientCredentials(oAuthFlow);
    return oAuthFlows;
  }

  @Bean
  OAuthFlow oAuthFlow(Scopes scopes, AppPropertiesConfig appPropertiesConfig) {
    OAuthFlow oAuthFlow = new OAuthFlow();
    oAuthFlow.setTokenUrl(appPropertiesConfig.getApiDocumentation().getTokenUrl());
    oAuthFlow.setScopes(scopes);
    return oAuthFlow;
  }

  @Bean
  Scopes scopes() {
    Scopes scopes = new Scopes();
    scopes.addString("books:get", "Retrieve books");
    scopes.addString("books:save", "Save new book");
    scopes.addString("books:delete", "Delete a book");
    scopes.addString("books:update", "Update book");
    scopes.addString("items:get", "Retrieve items");
    scopes.addString("items:upload", "Upload items");
    scopes.addString("items:delete", "Delete items");
    return scopes;
  }
}

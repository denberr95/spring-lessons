package com.personal.springlessons.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.SpecVersion;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.In;

@Configuration(proxyBeanMethods = false)
public class OpenAPIConfig {

    @Bean
    OpenAPI openAPI(Info info, SecurityScheme securityScheme) {
        OpenAPI openAPI = new OpenAPI();
        openAPI.setInfo(info);
        openAPI.setSpecVersion(SpecVersion.V31);
        return openAPI;
    }

    @Bean
    Info info() {
        Info info = new Info();
        info.setDescription("Open API Spring Lessons");
        info.setTitle("Spring Lessons");
        info.setVersion("1.0.0");
        return info;
    }

    @Bean
    SecurityScheme securityScheme(OAuthFlows oAuthFlows) {
        SecurityScheme securityScheme = new SecurityScheme();
        securityScheme.flows(oAuthFlows);
        securityScheme.setIn(In.HEADER);
        return securityScheme;
    }

    @Bean
    OAuthFlows oAuthFlows(Scopes scopes) {
        OAuthFlows oAuthFlows = new OAuthFlows();
        oAuthFlows.clientCredentials(new OAuthFlow().scopes(scopes));
        return oAuthFlows;
    }

    @Bean
    Scopes scopes() {
        Scopes scopes = new Scopes();
        scopes.addString("prova", "valore");
        return scopes;
    }
}

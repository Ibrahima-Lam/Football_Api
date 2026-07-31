package com.fscore.app.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI footballApiOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Football API")
                .description("Comprehensive Football Data Management API")
                .version("1.0.0"));
    }
}

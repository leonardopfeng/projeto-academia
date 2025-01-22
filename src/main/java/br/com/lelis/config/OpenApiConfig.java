package br.com.lelis.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI custonOpenAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("RESTful API with Java and Spring Boot 3")
                        .version("v1")
                        .description("Some description")
                        .termsOfService("https://pub.lelis.com.br/terms-of-service")
                        .license(new License().name("Apache 2.0")
                                .url("https://pub.lelis.com.br/my-courses")
                        )
                );
    }
}

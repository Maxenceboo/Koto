package com.koto.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI kotoOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Koto API")
                        .description("API pour les canaux, koto, utilisateurs, likes, etc.")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Koto Team")
                                .email("support@koto.example")))
                .externalDocs(new ExternalDocumentation()
                        .description("Docs techniques")
                        .url("https://example.com/docs"))
                // ✅ Partie sécurité JWT
                .schemaRequirement("bearerAuth", new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT"))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }
}

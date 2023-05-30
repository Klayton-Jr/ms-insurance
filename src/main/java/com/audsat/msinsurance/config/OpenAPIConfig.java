package com.audsat.msinsurance.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        Contact contact = new Contact();
        contact.setEmail("klaytonjuniordev@gmail.com");
        contact.setName("Klayton");

        Info info = new Info()
                .title("MS Insurance")
                .version("1.0.0")
                .contact(contact)
                .description("Description test");

        return new OpenAPI().info(info);
    }
}

package ru.krivonogova.autopark.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Autopark",
                description = "System for managers", version = "1.0.0",
                contact = @Contact(
                        name = "tatyana86",
                        url = "https://mysweetcar.ru"
                )
        )
)
public class OpenApiConfig {

}

package hu.gerab.greeting.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("LLM birthday greeting generator")
                        .version("0.1.0")
                        .description("Simple Swagger API for a simple LLM birthday greeting generator webapp"));
    }
}

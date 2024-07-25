package gift.utils.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Your API Title")
                .version("1.0")
                .description("Your API Description")
                .contact(new Contact().name("Your Name").email("your.email@example.com")))
            .addServersItem(new Server().url("/").description("Default Server URL"));
    }
}

// java
package movies.moviesservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Value("${springdoc.server-url:}")
    private String serverUrl;

    @Bean
    public OpenAPI customOpenAPI() {
        OpenAPI openAPI = new OpenAPI()
                .info(new Info()
                        .title("Movies Service API")
                        .version("v1.0")
                        .description("API for managing movies")
                        .contact(new Contact().name("Your Team").email("team@example.com")));

        if (serverUrl != null && !serverUrl.isBlank()) {
            openAPI.addServersItem(new Server().url(serverUrl));
        }

        return openAPI;
    }
}

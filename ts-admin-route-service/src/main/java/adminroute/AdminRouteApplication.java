package adminroute;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@OpenAPIDefinition(
    info = @Info(
        title = "Admin Route Service API",
        version = "1.0",
        description = "Admin Route Service API Documentation"
    )
)
public class AdminRouteApplication {
    public static void main(String[] args) {
        SpringApplication.run(AdminRouteApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}

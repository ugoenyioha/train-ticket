package adminorder;

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
        title = "Admin Order Service API",
        version = "1.0",
        description = "Admin Order Service API Documentation"
    )
)
public class AdminOrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(AdminOrderApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}

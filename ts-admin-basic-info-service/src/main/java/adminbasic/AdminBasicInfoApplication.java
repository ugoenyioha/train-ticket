package adminbasic;

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
        title = "Admin Basic Info Service API",
        version = "1.0",
        description = "Admin Basic Info Service API Documentation"
    )
)
public class AdminBasicInfoApplication {
    public static void main(String[] args) {
        SpringApplication.run(AdminBasicInfoApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}

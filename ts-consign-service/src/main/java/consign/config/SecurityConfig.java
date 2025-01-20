package consign.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import edu.fudan.common.security.jwt.JWTFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // Add logging filter before security checks
        http.addFilterBefore(new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, 
                                        HttpServletResponse response, 
                                        FilterChain chain) throws IOException, ServletException {
                logger.info("Request received at service: " + request.getRequestURI());
                logger.info("Authorization header at service: " + request.getHeader("Authorization"));
                chain.doFilter(request, response);
            }
        }, SecurityContextHolderFilter.class);
        
        http
            .csrf().disable()
            .httpBasic().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/consignservice/**").hasAnyRole("ADMIN", "USER")
                .requestMatchers(
                    "/swagger-ui.html",
                    "/webjars/**",
                    "/images/**",
                    "/configuration/**",
                    "/swagger-resources/**",
                    "/v2/**"
                ).permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(new JWTFilter(), UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling()
                .authenticationEntryPoint((request, response, authException) -> {
                    // Log the authentication failure
                    System.out.println("Authentication failed: " + authException.getMessage());
                    response.sendError(401, "Authentication failed: " + authException.getMessage());
                });
        http.headers().cacheControl();
        return http.build();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins(
                            "http://localhost:33117", 
                            "http://localhost:55117", 
                            "https://train-ticket.home.usableapps.io"
                        )
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS")
                        .allowedHeaders("*")
                        .exposedHeaders("Authorization")  // Explicitly expose Authorization header
                        .allowCredentials(false)  // Changed to true if you need to send cookies
                        .maxAge(3600);
            }
        };
    }   
}
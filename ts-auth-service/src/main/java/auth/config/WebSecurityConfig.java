package auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.http.HttpMethod;
import edu.fudan.common.security.jwt.JWTFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    @Qualifier("userDetailServiceImpl")
    private UserDetailsService userDetailsService;

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
            .userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder)
            .and()
            .build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .httpBasic().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            // Change the order of filter rules to be more specific
            .authorizeHttpRequests(auth -> auth
                // Public endpoints first
                .requestMatchers(
                    "/api/v1/auth",
                    "/api/v1/auth/hello",
                    "/api/v1/user/hello",
                    "/api/v1/users/login"  // Explicitly list login endpoint
                ).permitAll()
                // Admin endpoints
                .requestMatchers(HttpMethod.GET, "/api/v1/users").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/users/*").hasRole("ADMIN")
                .requestMatchers("/user/**").permitAll()
                // Swagger endpoints
                .requestMatchers(
                    "/swagger-ui.html",
                    "/webjars/**",
                    "/images/**",
                    "/configuration/**",
                    "/swagger-resources/**",
                    "/v2/**"
                ).permitAll()

                // All other endpoints need authentication
                .anyRequest().authenticated()
            )
            // Move JWT filter to run after authentication
            .addFilterBefore(new JWTFilter(), UsernamePasswordAuthenticationFilter.class);
        
        http.headers().cacheControl();
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:33117", "http://localhost:55117", "https://train-ticket.home.usableapps.io")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(false)
                        .maxAge(3600);
            }
        };
    }   
}

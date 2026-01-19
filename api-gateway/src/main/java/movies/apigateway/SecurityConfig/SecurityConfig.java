package movies.apigateway.SecurityConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                // 1. Disable CSRF (Standard for REST APIs)
                .csrf(ServerHttpSecurity.CsrfSpec::disable)

                // 2. Enable CORS (Uses the bean defined below)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // 3. Define URL Protections
                .authorizeExchange(exchanges -> exchanges
                        // Public Endpoints (Swagger, Eureka, Actuator)
                        .pathMatchers("/eureka/**", "/actuator/**").permitAll()
                        .pathMatchers(HttpMethod.OPTIONS).permitAll() // Allow pre-flight checks

                        .anyExchange().authenticated()
                )

                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> {}));

        return http.build();
    }

    /**
     * CORS Configuration
     * This is critical. Without this, your React/Next.js app cannot talk to the Gateway.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Allow your Frontend URL
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));

        // Allow all HTTP methods
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));

        // Allow all headers (Authorization, Content-Type, etc.)
        configuration.setAllowedHeaders(List.of("*"));

        // Allow sending cookies/credentials (Important for Auth)
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Apply this config to ALL routes handled by the Gateway
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
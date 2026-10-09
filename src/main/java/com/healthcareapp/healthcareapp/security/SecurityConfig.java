package com.healthcareapp.healthcareapp.security;

import com.healthcareapp.healthcareapp.services.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.HttpStatusAccessDeniedHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Value("${app.frontend-origin}")
    private String frontendOrigin;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)
            throws Exception {

        http
                // -------------------------
                // CORS
                // -------------------------
                .cors(Customizer.withDefaults())

                // -------------------------
                // CSRF
                // -------------------------
                .csrf(csrf -> csrf.disable())

                // -------------------------
                // Stateless JWT authentication
                // -------------------------
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                // -------------------------
                // Authorization rules
                // -------------------------
                .authorizeHttpRequests(auth -> auth

                        // Public authentication endpoints
                        .requestMatchers(
                                "/auth/signup",
                                "/auth/signin"
                        )
                        .permitAll()


                        // Must be logged in
                        .requestMatchers(
                                "/auth/me",
                                "/auth/signout"
                        )
                        .authenticated()


                        // -------------------------
                        // USERS
                        // -------------------------

                        .requestMatchers(
                                HttpMethod.GET,
                                "/users"
                        )
                        .hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.POST,
                                "/users"
                        )
                        .hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/users/**"
                        )
                        .hasRole("ADMIN")


                        // -------------------------
                        // WORKERS
                        // -------------------------

                        .requestMatchers(
                                "/worker/**"
                        )
                        .hasAnyRole(
                                "DOCTOR",
                                "ADMIN"
                        )


                        // Everything else requires login
                        .anyRequest()
                        .authenticated()
                )

                // -------------------------
                // 401 / 403 handling
                // -------------------------
                .exceptionHandling(exception -> exception

                        // 401 - user is not logged in / invalid JWT
                        .authenticationEntryPoint((request, response, authException) -> {

                            response.setStatus(HttpStatus.UNAUTHORIZED.value());
                            response.setContentType("application/json");

                            response.getWriter().write("""
                    {
                        "status": 401,
                        "error": "Unauthorized",
                        "message": "You must sign in before accessing this resource."
                    }
                    """);
                        })

                        // 403 - user is logged in but does not have permission
                        .accessDeniedHandler((request, response, accessDeniedException) -> {

                            response.setStatus(HttpStatus.FORBIDDEN.value());
                            response.setContentType("application/json");

                            response.getWriter().write("""
                    {
                        "status": 403,
                        "error": "Forbidden",
                        "message": "You do not have permission to access this resource."
                    }
                    """);
                        })
                )

                // -------------------------
                // JWT filter
                // -------------------------
                .addFilterBefore(
                        jwtAuthFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }


    // -------------------------
    // PASSWORD ENCODER
    // -------------------------

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    // -------------------------
    // AUTHENTICATION MANAGER
    // -------------------------

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config
    ) throws Exception {

        return config.getAuthenticationManager();
    }


    // -------------------------
    // CORS CONFIGURATION
    // -------------------------

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config =
                new CorsConfiguration();

        config.setAllowedOrigins(
                List.of(frontendOrigin)
        );

        config.setAllowedMethods(
                List.of(
                        "GET",
                        "POST",
                        "PUT",
                        "PATCH",
                        "DELETE",
                        "OPTIONS"
                )
        );

        config.setAllowedHeaders(
                List.of(
                        "Content-Type",
                        "Authorization"
                )
        );

        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
                "/**",
                config
        );

        return source;
    }
}
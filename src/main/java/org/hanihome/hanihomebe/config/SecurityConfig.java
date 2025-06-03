package org.hanihome.hanihomebe.config;


import lombok.RequiredArgsConstructor;
import org.hanihome.hanihomebe.auth.filter.JwtAuthenticationFilter;
import org.hanihome.hanihomebe.auth.service.AuthService;
import org.hanihome.hanihomebe.auth.util.JwtUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {


    private final JwtUtils jwtUtils;
    private final AuthService authService;
    private final RedisTemplate<String, String> redisTemplate;


    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtUtils, authService,redisTemplate);
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        return http
                .cors(cors -> {})
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/v1/auth/social/login",
                                "/v3/api-docs/**",      // Swagger API docs
                                "/swagger-ui/**",       // Swagger UI
                                "/swagger-ui.html",
                                "/health",
                                "/api/v1/properties",
                                "/api/v1/admin/**").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}

package org.hanihome.hanihomebe.security.config;


import lombok.RequiredArgsConstructor;
import org.hanihome.hanihomebe.security.auth.application.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(cors -> {})
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET,
                                "api/v1/properties/categories/**",
                                "/api/v1/viewings/categories/**",
                                "/api/v1/properties"
                        ).permitAll()
                        .requestMatchers(
                                "/api/v1/auth/social/login",
                                "/api/v1/auth/login",
                                "/api/v1/members/signup",
                                "/v3/api-docs/**",      // Swagger API docs
                                "/swagger-ui/**",       // Swagger UI
                                "/swagger-ui.html",
                                "/health",
                                "/api/v1/auth/refresh",
                                "/api/v1/members/check-nickname",
                                "/actuator/**",
                                "/error"
                        ).permitAll()
                        .anyRequest().authenticated()//위 url 제외하고는 인증 필요

                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    //로그인 AuthenticationManager =>authenticationManager.authenticate(UsernamePasswordAuthenticationToken) 이런 식으로 인증
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    //패스워드 인코더 등록
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

package com.claudialamas.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Configuration
public class SecurityConfig {

    @Value("${security.jwt.secret}")
    private String secret;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // <- stateless
                .and()
                .authorizeRequests()
                .antMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/actuator/**").permitAll()
                .antMatchers("/auth/login").permitAll()
                .antMatchers(HttpMethod.GET, "/api/public/**").permitAll()
                .antMatchers("/admin/error-report/**").permitAll()
                .antMatchers("/error").permitAll()
                .antMatchers("/api/**").authenticated()
                .anyRequest().denyAll()
                .and()
                .oauth2ResourceServer()
                .jwt().decoder(jwtDecoder());

        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        SecretKeySpec key = new SecretKeySpec(keyBytes, "HmacSHA256");
        return NimbusJwtDecoder
                .withSecretKey(key)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
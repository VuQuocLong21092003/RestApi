package com.example.RestApi.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final String[] PUBLIC_URL = {"/auth/login", "/auth/introspec", "/users"};

    private static final String SIGN = "5020f057d0d31c44d2397a3265c89b86b95a1903160610e290786cfe36e43e7b";

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests.requestMatchers(HttpMethod.POST, PUBLIC_URL).permitAll()
                                .requestMatchers(HttpMethod.DELETE, "/users")
                                .hasAnyAuthority("ADMIN")
                                .anyRequest().authenticated()
                );



        httpSecurity.oauth2ResourceServer(oauth2ResourceServer ->
                oauth2ResourceServer.jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder())));

        httpSecurity.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());
        return httpSecurity.build();
    }

    @Bean
    JwtDecoder jwtDecoder() {
        SecretKey secretKey = new SecretKeySpec(SIGN.getBytes(), "HS512");

        return NimbusJwtDecoder
                .withSecretKey(secretKey)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
    }


}

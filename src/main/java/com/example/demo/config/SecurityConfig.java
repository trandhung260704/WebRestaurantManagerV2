package com.example.demo.config;

import com.example.demo.jwt.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests((authorizeHttpRequests) ->
                                authorizeHttpRequests
                                        .requestMatchers(
                                                "/auth/**",
                                                "/v3/api-docs/**",
                                                "/swagger-ui/**",
                                                "/api/login",
                                                "/api/logout",
                                                "/api/users/**",
                                                "/api/foods",
                                                "/api/orders/**",
                                                "/api/bills/**",
                                                "/api/ingredients/**",
                                                "/api/discounts/**",
                                                "/api/auth/google/callback"

                                        ).permitAll()
                                        .anyRequest().authenticated()
                        //authorizeHttpRequests
                        //        .requestMatchers(
                        //                "/"
                        //                //"/auth/**",
                        //                //"/v3/api-docs/**",
                        //                //"/swagger-ui/**"
                        //        )
                        //        .permitAll()
                        //        .anyRequest()
                        //        .authenticated()
                );

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}

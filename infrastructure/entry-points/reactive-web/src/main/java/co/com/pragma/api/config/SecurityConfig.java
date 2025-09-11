package co.com.pragma.api.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;

import org.springframework.security.web.server.SecurityWebFilterChain;
import co.com.pragma.api.security.exception.CustomAccessDeniedHandler;
import co.com.pragma.api.security.exception.CustomAuthenticationEntry;
import co.com.pragma.api.security.filter.AuthFilter;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@Configuration
@AllArgsConstructor
public class SecurityConfig {

    private final AuthFilter authFilter;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CustomAuthenticationEntry authenticationEntry;

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(cors -> {
                })
                .authorizeExchange(exchanges -> exchanges

                        .pathMatchers("/login",
                                "/swagger",
                                "/swagger/**",
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/docs",
                                "/docs/**",
                                "/v3/api-docs",
                                "/v3/api-docs/**","/webjars/**").permitAll()
                        .anyExchange().authenticated()
                ).addFilterBefore(authFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .exceptionHandling(exceptions ->
                        exceptions.authenticationEntryPoint(authenticationEntry)
                                .accessDeniedHandler(accessDeniedHandler)
                )
                .build();

    }


}

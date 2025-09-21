package co.com.pragma.api.security.exception;


import co.com.pragma.api.dto.ApiResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

import static constants.MessageExceptions.*;

@Component
public class CustomAuthenticationEntry implements ServerAuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    @Autowired
    public CustomAuthenticationEntry(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException authException) {

        ApiResponseDto<Object> apiResponse =
                new ApiResponseDto<>(
                        authException.getMessage(),
                        MSG_UNAUTHORIZED,
                        HttpStatus.UNAUTHORIZED
                );


        byte[] bytes;
        try {
            bytes = objectMapper.writeValueAsBytes(apiResponse);
        } catch (Exception e) {
            bytes = ("{\"error\":\"Serialization failed\"}").getBytes(StandardCharsets.UTF_8);
        }

        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        return exchange.getResponse().writeWith(
                Mono.just(exchange.getResponse().bufferFactory().wrap(bytes))
        );
    }
}

package co.com.pragma.api.handler;

import co.com.pragma.api.dto.ApiResponseDto;
import co.com.pragma.model.solicitudes.exception.InvalidLoanTypeException;
import co.com.pragma.model.solicitudes.exception.Unauthorized;
import co.com.pragma.model.solicitudes.exception.UserNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@Order(-2)
public class GlobalExceptionHandler implements WebExceptionHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        log.info(ex.getClass().getName());
        var response = exchange.getResponse();
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ApiResponseDto<?> body;
        log.info(ex.getClass().getName());
        switch (ex) {
            case UserNotFoundException userEx -> {
                status = HttpStatus.NOT_FOUND;
                body = new ApiResponseDto<>(status.toString(), userEx.getMessage(), status);
            }
            case InvalidLoanTypeException illegalArgEx -> {
                status = HttpStatus.BAD_REQUEST;
                body = new ApiResponseDto<>(status.toString(), illegalArgEx.getMessage(), status);
            }
            case Unauthorized unauthorizedEx -> {
                status = HttpStatus.UNAUTHORIZED;
                body = new ApiResponseDto<>(status.toString(), unauthorizedEx.getMessage(), status);
            }
            default -> body = new ApiResponseDto<>(status.toString(), String.valueOf(ex), status);
        }
        response.setStatusCode(status);
        try {
            byte[] bytes = objectMapper.writeValueAsBytes(body);
            return response.writeWith(Mono.just(response.bufferFactory().wrap(bytes)));

        } catch (Exception e) {
            return response.setComplete();
        }
    }
}


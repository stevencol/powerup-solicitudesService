package co.com.pragma.feignauthentication.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import org.springframework.http.HttpStatus;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@JsonPropertyOrder({"data", "message", "error", "fields", "token", "page", "status"})
public record ExternalApiResponseDto<T>(T data,
                                        HttpStatus status,
                                        String message,
                                        String error,
                                        Map<String, Object> fields,
                                        String token) {


}

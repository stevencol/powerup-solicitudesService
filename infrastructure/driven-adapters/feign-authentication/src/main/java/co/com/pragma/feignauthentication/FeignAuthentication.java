package co.com.pragma.feignauthentication;

import co.com.pragma.feignauthentication.dto.ExternalApiResponseDto;
import co.com.pragma.feignauthentication.dto.ExternalUserDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;

@ReactiveFeignClient(name = "users-service", url = "${USERS_SERVICE_URL}")
public interface FeignAuthentication {

    @GetMapping("/api/users/document")
    Mono<ExternalApiResponseDto<ExternalUserDto>> findByDocumentNumber(@RequestHeader("Authorization") String token);


    @GetMapping("/api/users/document/{documentNumber}")
    Mono<ExternalApiResponseDto<ExternalUserDto>> findByDocumentNumber(@PathVariable("documentNumber") String documentNumber, @RequestHeader("Authorization") String token);
}

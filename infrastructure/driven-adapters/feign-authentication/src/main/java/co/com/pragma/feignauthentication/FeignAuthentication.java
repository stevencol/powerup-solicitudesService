package co.com.pragma.feignauthentication;

import co.com.pragma.feignauthentication.dto.ExternalApiResponseDto;
import co.com.pragma.feignauthentication.dto.ExternalUserDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;

@ReactiveFeignClient(name = "users-service", url = "${clients.users-service.url}")
public interface FeignAuthentication {

    @GetMapping("/api/users/{documentNumber}")
    Mono<ExternalApiResponseDto<ExternalUserDto>> findByDocumentNumber(@PathVariable("documentNumber") String documentNumber);

}

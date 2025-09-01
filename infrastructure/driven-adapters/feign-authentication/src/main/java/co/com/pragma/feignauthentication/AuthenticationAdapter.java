package co.com.pragma.feignauthentication;

import co.com.pragma.feignauthentication.dto.ExternalUserDto;
import co.com.pragma.feignauthentication.mapper.UserExternalDtoMapper;
import co.com.pragma.model.solicitudes.exception.UserNotFoundException;
import co.com.pragma.model.solicitudes.gateways.UserExternalRepository;
import co.com.pragma.model.solicitudes.model.UserModel;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import co.com.pragma.model.solicitudes.enums.*;

import static constants.MessageExceptions.*;

@RequiredArgsConstructor
@Repository
public class AuthenticationAdapter implements UserExternalRepository {


    private final FeignAuthentication feignAuthentication;
    private final UserExternalDtoMapper userExternalDtoMapper;

    @Override
    public Mono<UserModel> findUserByDocumentNumber(String documentNumber) {
        return feignAuthentication.findByDocumentNumber(documentNumber).map(externalApiResponseDto -> {
            ExternalUserDto userDto = externalApiResponseDto.data();
            return userExternalDtoMapper.userExternalDtoToUserModel(userDto);
        }).onErrorResume(FeignException.NotFound.class, ex -> {
            return Mono.error(new UserNotFoundException(String.format(MSG_NOT_FOUND, IdentifierType.DOCUMENT_NUMBER, documentNumber)));
        }).onErrorResume(FeignException.class, ex -> {
            return Mono.error(new RuntimeException(MSG_AUTH_SERVICE_ERROR + " :" + ex.getMessage()));
        });
    }
}

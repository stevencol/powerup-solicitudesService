package co.com.pragma.feignauthentication;

import co.com.pragma.feignauthentication.dto.ExternalUserDto;
import co.com.pragma.feignauthentication.mapper.UserExternalDtoMapper;
import co.com.pragma.model.solicitudes.exception.Unauthorized;
import co.com.pragma.model.solicitudes.exception.UserNotFoundException;
import co.com.pragma.model.solicitudes.gateways.UserExternalRepository;
import co.com.pragma.model.solicitudes.model.UserModel;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactivefeign.client.ReactiveFeignException;
import reactor.core.publisher.Mono;
import co.com.pragma.model.solicitudes.enums.*;


import static constants.MessageExceptions.*;

@RequiredArgsConstructor
@Repository
public class AuthenticationAdapter implements UserExternalRepository {


    private final FeignAuthentication feignAuthentication;
    private final UserExternalDtoMapper userExternalDtoMapper;

    @Override
    public Mono<UserModel> findUserByDocumentNumber(String token) {
        return feignAuthentication.findByDocumentNumber(token)
                .switchIfEmpty(Mono.error(new UserNotFoundException(String.format(MSG_NOT_FOUND, IdentifierType.DOCUMENT_NUMBER, ""))))
                .map(externalApiResponseDto -> {
                    ExternalUserDto userDto = externalApiResponseDto.data();
                    return userExternalDtoMapper.userExternalDtoToUserModel(userDto);
                }).onErrorResume(FeignException.NotFound.class, notFound -> Mono.error(new UserNotFoundException(String.format(MSG_NOT_FOUND, IdentifierType.DOCUMENT_NUMBER)))

                ).onErrorResume(ReactiveFeignException.class, reactiveFeign -> Mono.error(new RuntimeException(String.format(MSG_AUTH_SERVICE_ERROR, reactiveFeign.getCause()))))
                .onErrorResume(FeignException.Unauthorized.class, feign -> Mono.error(new Unauthorized(MSG_UNAUTHORIZED)))
                .onErrorResume(FeignException.class, feign -> Mono.error(new RuntimeException(MSG_AUTH_SERVICE_ERROR + " :" + feign.getMessage())))
                ;

    }

    @Override
    public Mono<UserModel> findUserByDocumentNumber(String documentNumber, String token) {

        return feignAuthentication.findByDocumentNumber(documentNumber, token)
                .switchIfEmpty(Mono.error(new UserNotFoundException(String.format(MSG_NOT_FOUND, IdentifierType.DOCUMENT_NUMBER, documentNumber))))
                .map(externalApiResponseDto -> {
                    ExternalUserDto userDto = externalApiResponseDto.data();
                    return userExternalDtoMapper.userExternalDtoToUserModel(userDto);
                }).onErrorResume(FeignException.NotFound.class, notFound -> Mono.error(new UserNotFoundException(String.format(MSG_NOT_FOUND, IdentifierType.DOCUMENT_NUMBER, documentNumber)))

                ).onErrorResume(ReactiveFeignException.class, reactiveFeign -> Mono.error(new RuntimeException(String.format(MSG_AUTH_SERVICE_ERROR, reactiveFeign.getCause()))))
                .onErrorResume(FeignException.Unauthorized.class, feign -> Mono.error(new Unauthorized(MSG_UNAUTHORIZED)))
                .onErrorResume(FeignException.class, feign -> Mono.error(new RuntimeException(MSG_AUTH_SERVICE_ERROR + " :" + feign.getMessage())))
                ;

    }
}


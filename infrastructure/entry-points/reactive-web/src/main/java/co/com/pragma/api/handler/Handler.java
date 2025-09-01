package co.com.pragma.api.handler;

import co.com.pragma.api.dto.ApiResponseDto;
import co.com.pragma.api.dto.SolicitudDto;
import co.com.pragma.api.mapper.SolicitudesModelMapper;
import co.com.pragma.model.solicitudes.exception.UserNotFoundException;
import co.com.pragma.model.solicitudes.model.SolicitudeModel;
import co.com.pragma.usecase.solicitudes.SolicitudesUseCase;
import co.com.pragma.usecase.solicitudes.UserExternalUseCase;
import constants.MessagesInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static co.com.pragma.api.helper.ResponseHelper.responseHelper;

import static constants.MessagesInfo.*;
import static constants.MessageExceptions.*;

@Component

@Slf4j
@AllArgsConstructor
public class Handler {

    private final UserExternalUseCase userExternalUseCase;
    private final SolicitudesUseCase solicitudesUseCases;
    private final SolicitudesModelMapper solicitudMapper;

    public Mono<ServerResponse> listenGETUseCase(ServerRequest serverRequest) {
        String documentNumber = serverRequest.pathVariable("documentNumber");
        log.info("Document Number: {}: ", documentNumber);
        return userExternalUseCase.findUserByDocumentNumber(documentNumber)
                .flatMap(userModel -> ServerResponse.ok().bodyValue(userModel))
                .switchIfEmpty(ServerResponse.ok().bodyValue(documentNumber));
    }

    public Mono<ServerResponse> listenGETOtherUseCase(ServerRequest serverRequest) {
        return ServerResponse.ok().bodyValue("");
    }

    public Mono<ServerResponse> listenPOSTUseCase(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(SolicitudDto.class)
                .flatMap(solicitud -> {
                    log.info("dto: {}: ", solicitud);
                    SolicitudeModel solicitudeModel = solicitudMapper.toModel(solicitud);
                    log.info("model: {}: ", solicitudeModel);
                    solicitudeModel.setId(null);
                    return userExternalUseCase.findUserByDocumentNumber(solicitudeModel.getUserDocumentNumber()).onErrorResume(err -> {
                        if (err instanceof UserNotFoundException) {
                            return Mono.error(new UserNotFoundException(err.getMessage()));
                        }
                        return Mono.error(new RuntimeException(MSG_AUTH_SERVICE_ERROR + " :" + err.getMessage()));
                    }).flatMap(user -> {
                        return solicitudesUseCases.createSolicitud(solicitudeModel)
                                .flatMap(solicitudRequest -> {
                                    log.info("Result: {}: ", solicitudRequest);
                                    SolicitudDto solicitudDto = solicitudMapper.toDto(solicitudRequest, user);
                                    return responseHelper(new ApiResponseDto<SolicitudDto>(solicitudDto, MSG_OPERATION_SUCCESS, HttpStatus.CREATED));
                                });
                    });

                });
    }
}

package co.com.pragma.api.handler;

import co.com.pragma.api.dto.ApiResponseDto;
import co.com.pragma.api.dto.SolicitudDto;
import co.com.pragma.api.dto.UserPrincipal;
import co.com.pragma.api.mapper.PageMapper;
import co.com.pragma.api.mapper.SolicitudesModelMapper;
import co.com.pragma.model.solicitudes.enums.Status;
import co.com.pragma.model.solicitudes.model.PageModel;
import co.com.pragma.model.solicitudes.model.SolicitudModel;
import co.com.pragma.model.solicitudes.model.UserModel;
import co.com.pragma.usecase.solicitudes.SolicitudesUseCase;
import co.com.pragma.usecase.solicitudes.UserExternalUseCase;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static co.com.pragma.api.helper.ResponseHelper.responseHelper;

import static constants.MessagesInfo.*;

@Component

@Slf4j
@AllArgsConstructor
public class SolicitudesHandler {

    private final UserExternalUseCase userExternalUseCase;
    private final SolicitudesUseCase solicitudesUseCases;
    private final SolicitudesModelMapper solicitudMapper;
    private final PageMapper pageMapper;


    @PreAuthorize("hasAnyAuthority('client')")
    public Mono<ServerResponse> createSolicitud(ServerRequest serverRequest) {
        return ReactiveSecurityContextHolder.getContext().flatMap(securityContext -> {
            UserPrincipal userPrincipal = (UserPrincipal) securityContext.getAuthentication().getPrincipal();
            return serverRequest.bodyToMono(SolicitudDto.class).flatMap(solicitud -> {
                log.info("dto: {}: ", solicitud);
                SolicitudModel solicitudModel = solicitudMapper.toModel(solicitud);
                solicitudModel.setUserDocumentNumber(userPrincipal.getDocumentNumber());
                log.info("model: {}: ", solicitudModel);
                solicitudModel.setStatus(Status.PENDING_REVIEW);
                solicitudModel.setId(null);
                if (solicitud.validationAutomatic() == null) {
                    solicitudModel.setValidationAutomatic(false);
                }
                String authHeader = serverRequest.headers().firstHeader("Authorization");
                return userExternalUseCase.findUserByDocumentNumber(authHeader).flatMap(user -> solicitudesUseCases.createSolicitud(solicitudModel, user).flatMap(solicitudRequest -> {
                    log.info("Result: {}: ", solicitudRequest);
                    SolicitudDto solicitudDto = solicitudMapper.toDto(solicitudRequest, user);
                    return responseHelper(new ApiResponseDto<SolicitudDto>(solicitudDto, MSG_OPERATION_SUCCESS, HttpStatus.CREATED));
                }));

            });
        });
    }

    //@PreAuthorize("hasAnyAuthority('asessor')")
    public Mono<ServerResponse> updateSolicitud(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(SolicitudDto.class).flatMap(solicitudDto -> {
            SolicitudModel solicitudModel = solicitudMapper.toModel(solicitudDto);
            String authHeader = serverRequest.headers().firstHeader("Authorization");
            return solicitudesUseCases.updateStatus(solicitudModel, authHeader).flatMap(SolicitudModel -> {
                SolicitudDto solicitudResponse = solicitudMapper.toDto(SolicitudModel, null);
                return responseHelper(new ApiResponseDto<>(solicitudResponse, MSG_OPERATION_SUCCESS, HttpStatus.OK));

            });
        });
    }

    @PreAuthorize("hasAnyAuthority('admin')")
    public Mono<ServerResponse> findAllWithPagination(ServerRequest serverRequest) {

        int page = Integer.parseInt(serverRequest.queryParam("page").orElse("0"));
        int size = Integer.parseInt(serverRequest.queryParam("size").orElse("10"));
        String status = serverRequest.queryParam("status").orElse("PENDING_REVIEW");

        return solicitudesUseCases.findAllWithPagination(status, page, size).flatMap(solicitudPage -> {

            List<SolicitudModel> SolicitudModels = (List<SolicitudModel>) solicitudPage.get("data");
            PageModel pageModels = (PageModel) solicitudPage.get("page");
            String authHeader = serverRequest.headers().firstHeader("Authorization");

            return Flux.fromIterable(SolicitudModels).flatMap(solicitud -> userExternalUseCase.findUserByDocumentNumber(solicitud.getUserDocumentNumber(), authHeader)
                    .onErrorResume(e->Mono.just((UserModel) new UserModel()))
                    .map(userModel -> solicitudMapper.toDto(solicitud, userModel)

            )).collectList().flatMap(solicitudDtos -> responseHelper(new ApiResponseDto<List<SolicitudDto>>(solicitudDtos, HttpStatus.OK, pageMapper.toPageDto(pageModels), MSG_OPERATION_SUCCESS))


            );
        });


    }
}

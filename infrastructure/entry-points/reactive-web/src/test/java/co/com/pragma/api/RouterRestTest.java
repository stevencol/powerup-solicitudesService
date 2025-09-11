package co.com.pragma.api;

import co.com.pragma.api.config.SecurityConfig;
import co.com.pragma.api.dto.ApiResponseDto;
import co.com.pragma.api.dto.SolicitudDto;
import co.com.pragma.api.dto.UserDto;
import co.com.pragma.api.handler.SolicitudesHandler;
import co.com.pragma.api.mapper.SolicitudesModelMapper;
import co.com.pragma.api.router.RouterRest;
import co.com.pragma.model.solicitudes.enums.LoanType;
import co.com.pragma.model.solicitudes.enums.Status;
import co.com.pragma.model.solicitudes.gateways.SolicitudesRepository;
import co.com.pragma.model.solicitudes.gateways.UserExternalRepository;
import co.com.pragma.model.solicitudes.model.SolicitudModel;
import co.com.pragma.usecase.solicitudes.SolicitudesUseCase;
import co.com.pragma.usecase.solicitudes.UserExternalUseCase;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;


@ContextConfiguration(classes = {RouterRest.class, SolicitudesHandler.class, SecurityConfig.class})
@WebFluxTest
class RouterRestTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private UserExternalUseCase userExternalUseCase;

    @MockBean
    private SolicitudesRepository solicitudesRepository;

    @MockBean
    private SolicitudesUseCase solicitudesUseCase;


    @MockBean
    private UserExternalRepository userExternalRepository;

    @MockBean
    private SolicitudesModelMapper solicitudMapper;
    @MockBean
    private SolicitudesHandler solicitudesHandler;
    @Test
    void testListenGETUseCase() {
        when(solicitudesHandler.createSolicitud(any(ServerRequest.class)))
                .thenReturn(getApiResponse());

        when(solicitudesUseCase.createSolicitud(getSolicitudModel(),null)).thenReturn(getSoliciatudModel());
        webTestClient.post()
                .uri("/api/solicitudes")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(SolicitudModel.class)
                .value(apiResponseDto ->
                        Assertions.assertThat(apiResponseDto).isNotNull()

                );
    }

    private SolicitudDto getSolicitudDto() {
        return new SolicitudDto(
                17L,
                new UserDto(

                        44L,
                        "no debe estar vacío",
                        "1994-05-24",
                        "1994-05-24",
                        "3016135085",
                        "nassdasdadaffsdffsdslos",
                        LocalDate.of(1994, 5, 24),
                        "ssssss",
                        "ssssss",
                        "ssssss",
                        new BigDecimal("5000000.0"),
                        "ssssss"


                )
                , new BigDecimal("5000000.0"), 5, "PENDING_REVIEW",
                "2024-06-12T20:45:26.821Z",
                1.2,
                false
        );
    }


    private SolicitudModel getSolicitudModel() {
        return new SolicitudModel(
                null,
                "12313123",
                new BigDecimal("5000000.0"),
                5,
                LoanType.PERSONAL,
                Status.PENDING_REVIEW,
                1.2,
                false
        );
    }

    private Mono<SolicitudModel> getSoliciatudModel() {

        return Mono.just(getSolicitudModel());
    }


    private Mono<ServerResponse> getApiResponse() {
        ApiResponseDto<SolicitudDto> response = ApiResponseDto.<SolicitudDto>builder()
                .message("Solicitud creada exitosamente")
                .data(getSolicitudDto())
                .build();

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(response);
    }
}

package co.com.pragma.api.config;

import co.com.pragma.api.dto.SolicitudDto;
import co.com.pragma.api.handler.SolicitudesHandler;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
@Configuration
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class ApiDocsConfig {


    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/solicitudes",
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    consumes = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.POST,
                    beanClass = SolicitudesHandler.class,
                    beanMethod = "createSolicitud",
                    operation = @Operation(
                            operationId = "createSolicitud",
                            summary = "Crear una nueva solicitud",
                            tags = {"Solicitudes"},
                            security = {@SecurityRequirement(name = "bearerAuth")},
                            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                    description = "Datos de la solicitud",
                                    required = true,
                                    content = @Content(
                                            schema = @Schema(implementation = SolicitudDto.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(responseCode = "201", description = "Solicitud creada exitosamente"),
                                    @ApiResponse(responseCode = "401", description = "No autorizado"),
                                    @ApiResponse(responseCode = "403", description = "Acceso denegado"),
                                    @ApiResponse(responseCode = "500", description = "Error interno")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/solicitudes",
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.GET,
                    beanClass = SolicitudesHandler.class,
                    beanMethod = "findAllWithPagination",
                    operation = @Operation(
                            operationId = "findAllWithPagination",
                            summary = "Listar solicitudes con paginación",
                            tags = {"Solicitudes"},
                            security = {@SecurityRequirement(name = "bearerAuth")},
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Solicitudes obtenidas correctamente"),
                                    @ApiResponse(responseCode = "401", description = "No autorizado"),
                                    @ApiResponse(responseCode = "403", description = "Acceso denegado"),
                                    @ApiResponse(responseCode = "500", description = "Error interno")
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> solicitudDocsRoutes() {
        return RouterFunctions.route(request -> false, req -> ServerResponse.ok().build());
    }

}

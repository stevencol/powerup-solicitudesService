package co.com.pragma.api.router;

import co.com.pragma.api.handler.SolicitudesHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {
    @Bean
    public RouterFunction<ServerResponse> routerFunction(SolicitudesHandler solicitudesHandler) {
        return route(POST("/solicitudes").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), solicitudesHandler::createSolicitud)
                .andRoute(GET("/solicitudes").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), solicitudesHandler::findAllWithPagination)
                .andRoute(PUT("/solicitudes").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), solicitudesHandler::updateSolicitud);
    }
}

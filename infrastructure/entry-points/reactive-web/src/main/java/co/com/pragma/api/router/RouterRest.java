package co.com.pragma.api.router;

import co.com.pragma.api.handler.Handler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {
    @Bean
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(GET("/api/usecase/path/{documentNumber}").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::listenGETUseCase)
                .andRoute(POST("/api/usecase/otherpath").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::listenPOSTUseCase);
    }
}

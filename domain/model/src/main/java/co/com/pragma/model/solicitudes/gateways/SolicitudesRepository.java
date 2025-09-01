package co.com.pragma.model.solicitudes.gateways;

import co.com.pragma.model.solicitudes.model.SolicitudeModel;
import reactor.core.publisher.Mono;

public interface SolicitudesRepository {

    Mono<SolicitudeModel> createSolicitud(SolicitudeModel userModel);
}

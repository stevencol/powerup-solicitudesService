package co.com.pragma.model.solicitudes.gateways;

import co.com.pragma.model.solicitudes.model.NotificationMessageModel;
import co.com.pragma.model.solicitudes.model.SolicitudModel;
import reactor.core.publisher.Mono;

public interface SqsReporteRepository {

    Mono<Void> sendSolicitud(SolicitudModel solicitudModel);
}

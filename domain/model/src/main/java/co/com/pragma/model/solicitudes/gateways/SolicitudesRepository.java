package co.com.pragma.model.solicitudes.gateways;

import co.com.pragma.model.solicitudes.model.SolicitudModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SolicitudesRepository {

    Mono<SolicitudModel> createSolicitud(SolicitudModel solicitud);
    Mono<SolicitudModel> updateSolicitud(SolicitudModel solicitud);
    Flux<SolicitudModel> findByStatusWithPagination(String status, int limit, long offset);
    Flux<SolicitudModel> finByDocumentNumber(String documentNumber);
    Mono<Long> countByStatus(String status);
    Mono<SolicitudModel> findById(Long id);
}

package co.com.pragma.usecase.solicitudes;

import co.com.pragma.model.solicitudes.gateways.SolicitudesRepository;
import co.com.pragma.model.solicitudes.model.SolicitudeModel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class SolicitudesUseCase {


    private final SolicitudesRepository solicitudesRepository;

    public Mono<SolicitudeModel> createSolicitud(SolicitudeModel solicitudeModel) {
        return solicitudesRepository.createSolicitud(solicitudeModel);
    }
}

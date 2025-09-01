package co.com.pragma.r2dbc;

import co.com.pragma.model.solicitudes.gateways.SolicitudesRepository;
import co.com.pragma.model.solicitudes.model.SolicitudeModel;
import lombok.AllArgsConstructor;
import co.com.pragma.r2dbc.mapper.SolicitudMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@AllArgsConstructor
public class MyReactiveRepositoryAdapter implements SolicitudesRepository {


    private final R2BDSolicitudesRepository solicitudesRepository;
    private final SolicitudMapper solicitudMapper;

    @Override
    public Mono<SolicitudeModel> createSolicitud(SolicitudeModel solicitud) {
        return solicitudesRepository.save(solicitudMapper.toEntity(solicitud))
                .map(solicitudMapper::toModel);
    }
}

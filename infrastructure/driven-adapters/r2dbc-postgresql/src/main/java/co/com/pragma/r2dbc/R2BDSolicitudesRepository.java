package co.com.pragma.r2dbc;

import co.com.pragma.r2dbc.entity.SolicitudEntity;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;


public interface R2BDSolicitudesRepository extends ReactiveCrudRepository<SolicitudEntity, Long> {

    Flux<SolicitudEntity> findByUserDocumentNumber(String idCliente);


}

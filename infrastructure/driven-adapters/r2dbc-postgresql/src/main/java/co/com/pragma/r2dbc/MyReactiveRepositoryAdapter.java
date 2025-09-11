package co.com.pragma.r2dbc;

import co.com.pragma.model.solicitudes.gateways.SolicitudesRepository;
import co.com.pragma.model.solicitudes.model.SolicitudModel;
import co.com.pragma.r2dbc.entity.SolicitudEntity;
import exceptions.SolicitudesNoFoundException;
import lombok.AllArgsConstructor;
import co.com.pragma.r2dbc.mapper.SolicitudMapper;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static constants.MessageExceptions.*;

@Repository
@AllArgsConstructor
public class MyReactiveRepositoryAdapter implements SolicitudesRepository {


    private final R2BDSolicitudesRepository r2BDRepository;
    private final SolicitudMapper solicitudMapper;
    private final R2dbcEntityTemplate entityTemplate;

    @Override
    public Mono<SolicitudModel> createSolicitud(SolicitudModel solicitud) {
        return r2BDRepository.save(solicitudMapper.toEntity(solicitud))
                .map(solicitudMapper::toModel);
    }

    @Override
    public Mono<SolicitudModel> updateSolicitud(SolicitudModel solicitud) {
        return r2BDRepository.save(solicitudMapper.toEntity(solicitud))
                .map(solicitudMapper::toModel);
    }

    @Override
    public Flux<SolicitudModel> findByStatusWithPagination(String status, int limit, long offset) {
        Query dataQuery = Query.query(Criteria.where("status").is(status))
                .limit(limit)
                .offset(offset);


        return entityTemplate.select(SolicitudEntity.class)
                .matching(dataQuery)
                .all()
                .map(solicitudMapper::toModel);

    }

    @Override
    public Flux<SolicitudModel> finByDocumentNumber(String documentNumber) {
        return r2BDRepository.findByUserDocumentNumber(documentNumber)
                .switchIfEmpty(Mono.error(new SolicitudesNoFoundException(MSG_NOT_FOUND)))
                .map(solicitudMapper::toModel);
    }

    public Mono<Long> countByStatus(String status) {
        Query countQuery = Query.query(Criteria.where("status").is(status));
        return entityTemplate.count(countQuery, SolicitudEntity.class);
    }

    @Override
    public Mono<SolicitudModel> findById(Long id) {
        return r2BDRepository.findById(id)
                .switchIfEmpty(Mono.error(new SolicitudesNoFoundException(MSG_NOT_FOUND)))
                .map(solicitudMapper::toModel);
    }
}

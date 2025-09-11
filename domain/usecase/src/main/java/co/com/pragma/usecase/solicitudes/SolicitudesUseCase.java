package co.com.pragma.usecase.solicitudes;

import co.com.pragma.model.solicitudes.enums.Status;
import co.com.pragma.model.solicitudes.gateways.SolicitudesRepository;
import co.com.pragma.model.solicitudes.gateways.SqsEndeudaminetoRepository;
import co.com.pragma.model.solicitudes.gateways.SqsSendRepository;
import co.com.pragma.model.solicitudes.gateways.UserExternalRepository;
import co.com.pragma.model.solicitudes.model.NotificationMessageModel;
import co.com.pragma.model.solicitudes.model.PageModel;
import co.com.pragma.model.solicitudes.model.SolicitudModel;
import co.com.pragma.model.solicitudes.model.UserModel;
import co.com.pragma.model.solicitudes.model.sqs.SolicitudSqsModel;
import co.com.pragma.model.solicitudes.model.sqs.SqsPlayLoadModel;
import co.com.pragma.model.solicitudes.model.sqs.UserSQSModel;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class SolicitudesUseCase {


    private final SolicitudesRepository solicitudesRepository;
    private final UserExternalRepository userExternalRepository;
    private final SqsSendRepository sqsSendRepository;
    private final SqsEndeudaminetoRepository sqsEndeudamineto;


    public Mono<SolicitudModel> createSolicitud(SolicitudModel solicitudModel, UserModel userModel) {


        return solicitudesRepository.createSolicitud(solicitudModel).flatMap(solicitud -> {

                    if (solicitudModel.isValidationAutomatic()) {

                        UserSQSModel userSQSModel = new UserSQSModel(
                                userModel.getId(),
                                userModel.getFirstName(),
                                userModel.getMiddleName(),
                                userModel.getLastName(),
                                userModel.getSecondLastName(),
                                userModel.getOtherLastName(),
                                userModel.getBirthDate().toString(),
                                userModel.getAddress(),
                                userModel.getPhoneNumber(),
                                userModel.getEmail(),
                                userModel.getBaseSalary(),
                                userModel.getDocumentNumber());


                        SolicitudSqsModel solicitudSqsModel = new SolicitudSqsModel(
                                solicitud.getId(),
                                userSQSModel,
                                solicitud.getMount(),
                                solicitud.getTermInMonths(),
                                solicitud.getLoanType().name(),
                                solicitud.getStatus().name(),
                                solicitud.getInterestRate()
                        );

                        return solicitudesRepository.finByDocumentNumber(userSQSModel.getDocumentNumber())
                                .collectList()
                                .flatMap(solicitudes -> {
                                    SqsPlayLoadModel sqsPlayLoadModel = new SqsPlayLoadModel(solicitudSqsModel, solicitudes);
                                    return sqsEndeudamineto.evaluate(sqsPlayLoadModel).then(Mono.just(solicitud));
                                });


                    } else {
                        return Mono.just(solicitud);
                    }

                }

        );
    }


    public Mono<SolicitudModel> updateStatus(SolicitudModel solicitudModel, String token) {
        return solicitudesRepository.findById(solicitudModel.getId())
                .flatMap(solicitud ->
                        userExternalRepository.findUserByDocumentNumber(solicitud.getUserDocumentNumber(), token)
                                .flatMap(user -> {
                                    solicitud.setStatus(solicitudModel.getStatus());
                                    NotificationMessageModel notificationMessageModel =
                                            new NotificationMessageModel(
                                                    solicitud.getId(),
                                                    solicitud.getStatus().name(),
                                                    user.getEmail(),
                                                    user.getFirstName() + " " + user.getLastName(),
                                                    solicitud.getMount()
                                            );

                                    return sqsSendRepository.sendMessage(notificationMessageModel)
                                            .then(solicitudesRepository.updateSolicitud(solicitud));
                                })
                );
    }


    public Mono<Map<String, Object>> findAllWithPagination(String status, int page, int size) {
        final int limit = size;
        final long offset = (long) page * size;

        Mono<List<SolicitudModel>> solicitudes = solicitudesRepository
                .findByStatusWithPagination(status, limit, offset).collectList();

        Mono<Long> totalElements = solicitudesRepository.countByStatus(status);

        return Mono.zip(solicitudes, totalElements)
                .map(tuple -> {
                    List<SolicitudModel> data = tuple.getT1();
                    Long total = tuple.getT2();
                    PageModel pageModel = new PageModel(total, page, size);
                    Map<String, Object> response = new HashMap<>();
                    response.put("data", data);
                    response.put("page", pageModel);
                    return response;
                });
    }


}

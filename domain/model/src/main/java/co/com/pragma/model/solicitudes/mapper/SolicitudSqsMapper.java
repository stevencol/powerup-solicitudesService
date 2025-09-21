package co.com.pragma.model.solicitudes.mapper;

import co.com.pragma.model.solicitudes.model.SolicitudModel;
import co.com.pragma.model.solicitudes.model.sqs.SolicitudSqsModel;
import co.com.pragma.model.solicitudes.model.sqs.UserSQSModel;

public class SolicitudSqsMapper {

    public static SolicitudSqsModel toSolicitudSqsModel(SolicitudModel solicitudModel, UserSQSModel userSQSModel) {

        if (solicitudModel == null) {
            return null;
        }
        return new SolicitudSqsModel(
                solicitudModel.getId(),
                userSQSModel,
                solicitudModel.getMount(),
                solicitudModel.getTermInMonths(),
                solicitudModel.getLoanType().name(),
                solicitudModel.getStatus().name(),
                solicitudModel.getInterestRate()
        );

    }
}

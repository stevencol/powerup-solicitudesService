package co.com.pragma.model.solicitudes.model.sqs;

import co.com.pragma.model.solicitudes.model.SolicitudModel;

import java.util.List;

public class SqsPlayLoadModel {

    SolicitudSqsModel solicitudDto;
    List<SolicitudModel> solicitudesDtos;


    public SqsPlayLoadModel(SolicitudSqsModel solicitudDto, List<SolicitudModel> solicitudesDtos) {
        this.solicitudDto = solicitudDto;
        this.solicitudesDtos = solicitudesDtos;
    }

    public SolicitudSqsModel getSolicitudDto() {
        return solicitudDto;
    }

    public void setSolicitudDto(SolicitudSqsModel solicitudDto) {
        this.solicitudDto = solicitudDto;
    }

    public List<SolicitudModel> getSolicitudesDtos() {
        return solicitudesDtos;
    }

    public void setSolicitudesDtos(List<SolicitudModel> solicitudesDtos) {
        this.solicitudesDtos = solicitudesDtos;
    }
}

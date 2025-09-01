package co.com.pragma.r2dbc.mapper;

import co.com.pragma.model.solicitudes.enums.LoanType;
import co.com.pragma.model.solicitudes.model.SolicitudeModel;
import co.com.pragma.r2dbc.entity.SolicitudEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SolicitudMapper {

    @Mapping(source = "loanType", target = "loanType", qualifiedByName = "stringToEnum")
    SolicitudeModel toModel(SolicitudEntity solicitud);

    @Mapping(source = "loanType", target = "loanType", qualifiedByName = "enumToString")
    SolicitudEntity toEntity(SolicitudeModel solicitud);

    @Named("enumToString")
    default String enumToString(LoanType loanType) {
        return loanType != null ? loanType.name() : null;
    }

    @Named("stringToEnum")
    default LoanType stringToEnum(String loanType) {
        return loanType != null ? LoanType.valueOf(loanType.toUpperCase()) : null;
    }

}

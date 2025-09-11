package co.com.pragma.api.mapper;

import co.com.pragma.api.dto.SolicitudDto;
import co.com.pragma.model.solicitudes.enums.LoanType;
import co.com.pragma.model.solicitudes.exception.InvalidLoanTypeException;
import co.com.pragma.model.solicitudes.model.SolicitudModel;
import co.com.pragma.model.solicitudes.model.UserModel;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SolicitudesModelMapper {

    /*@Mapping(source = "loanType", target = "loanType", qualifiedByName = "mapLoanType")
    @Mapping(source = "userDto.documentNumber", target = "userDocumentNumber")

     */
    SolicitudModel toModel(SolicitudDto dto);

    SolicitudDto toDto(SolicitudModel model);

    @Mapping(source = "model.id", target = "id")
    @Mapping(source = "user", target = "userDto")
    SolicitudDto toDto(SolicitudModel model, UserModel user);

    @Named("mapLoanType")
    default LoanType mapLoanType(String value) {

        try {
            return LoanType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidLoanTypeException("Invalid loan type: " + value);

        }
    }
}

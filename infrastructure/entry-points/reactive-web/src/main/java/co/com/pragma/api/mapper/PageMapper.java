package co.com.pragma.api.mapper;

import co.com.pragma.api.dto.PageDto;
import co.com.pragma.api.dto.SolicitudDto;
import co.com.pragma.model.solicitudes.model.PageModel;
import co.com.pragma.model.solicitudes.model.SolicitudModel;
import co.com.pragma.model.solicitudes.model.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PageMapper {

    PageDto toPageDto(PageModel pageModel);
}

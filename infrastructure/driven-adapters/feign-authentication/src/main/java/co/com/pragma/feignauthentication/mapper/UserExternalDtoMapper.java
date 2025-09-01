package co.com.pragma.feignauthentication.mapper;

import co.com.pragma.feignauthentication.dto.ExternalUserDto;
import co.com.pragma.model.solicitudes.model.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserExternalDtoMapper {

    UserModel userExternalDtoToUserModel(ExternalUserDto userExternalDto);

    ExternalUserDto userModelToUserExternalDto(UserModel userModel);
}

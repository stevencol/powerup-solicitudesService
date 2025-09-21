package co.com.pragma.model.solicitudes.mapper;

import co.com.pragma.model.solicitudes.model.UserModel;
import co.com.pragma.model.solicitudes.model.sqs.UserSQSModel;

public class UserModelSqsMapper {

    public static UserSQSModel toUserSQSModel(UserModel userModel) {

        if(userModel == null) return null;
 return  new UserSQSModel(
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

    }
}

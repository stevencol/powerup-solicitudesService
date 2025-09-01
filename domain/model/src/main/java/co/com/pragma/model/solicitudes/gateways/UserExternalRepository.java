package co.com.pragma.model.solicitudes.gateways;

import co.com.pragma.model.solicitudes.model.UserModel;
import reactor.core.publisher.Mono;

public interface UserExternalRepository {

    Mono<UserModel> findUserByDocumentNumber(String documentNumber);
}

package co.com.pragma.usecase.solicitudes;

import co.com.pragma.model.solicitudes.gateways.UserExternalRepository;
import co.com.pragma.model.solicitudes.model.UserModel;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor

public class UserExternalUseCase {


    private final UserExternalRepository userExternalRepository;

    public Mono<UserModel> findUserByDocumentNumber(String documentNumber, String token) {

        return userExternalRepository. findUserByDocumentNumber(documentNumber, token);
    }

    public Mono<UserModel> findUserByDocumentNumber(String token) {

        return userExternalRepository.findUserByDocumentNumber(token);
    }
}

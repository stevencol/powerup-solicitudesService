package co.com.pragma.usecase.solicitudes;

import co.com.pragma.model.solicitudes.exception.UserNotFoundException;
import co.com.pragma.model.solicitudes.gateways.UserExternalRepository;
import co.com.pragma.model.solicitudes.model.UserModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.verify;

import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;

class UserExternalUseCaseTest {

    @Mock
    private UserExternalRepository userExternalRepository;

    @InjectMocks
    private UserExternalUseCase userExternalUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void findUserByDocumentNumber_Success() {
        String documentNumber = "123456789";
        UserModel userModel = getUserModelTest();
        userModel.setDocumentNumber(documentNumber);

        when(userExternalRepository.findUserByDocumentNumber(documentNumber,""))
                .thenReturn(Mono.just(userModel));

        StepVerifier.create(userExternalUseCase.findUserByDocumentNumber(documentNumber,""))
                .expectNextMatches(user -> user.getDocumentNumber().equals(documentNumber))
                .verifyComplete();

        verify(userExternalRepository).findUserByDocumentNumber(documentNumber,"");
    }

    @Test
    void findUserByEntityNotFound() {
        String documentNumber = "000000000";

        when(userExternalRepository.findUserByDocumentNumber(documentNumber,""))
                .thenReturn(Mono.error(new UserNotFoundException("Usuario no encontrado")));

        StepVerifier.create(userExternalUseCase.findUserByDocumentNumber(documentNumber,""))
                .expectErrorMatches(throwable ->
                        throwable instanceof UserNotFoundException &&
                                throwable.getMessage().equals("Usuario no encontrado"))
                .verify();

        verify(userExternalRepository).findUserByDocumentNumber(documentNumber,"");
    }


    @Test
    void findUserByDocumentNumber_NotFound() {
        String documentNumber = "987654321";

        when(userExternalRepository.findUserByDocumentNumber(documentNumber,""))
                .thenReturn(Mono.empty());

        StepVerifier.create(userExternalUseCase.findUserByDocumentNumber(documentNumber,""))
                .verifyComplete();

        verify(userExternalRepository).findUserByDocumentNumber(documentNumber,"");
    }


    private UserModel getUserModelTest() {
        UserModel user = new UserModel();
        user.setId(1L);
        user.setFirstName("Steven");
        user.setMiddleName("Andres");
        user.setLastName("Gomez");
        user.setSecondLastName("Lopez");
        user.setOtherLastName("Martinez");
        user.setBirthDate(LocalDate.of(1995, 5, 20));
        user.setAddress("Cra 7 # 45-67");
        user.setPhoneNumber("3216549870");
        user.setEmail("steven@mail.com");
        user.setBaseSalary(new BigDecimal("2500.00"));
        user.setDocumentNumber("123456789");
        return user;
    }
}

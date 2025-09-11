package co.com.pragma.usecase.solicitudes;

import co.com.pragma.model.solicitudes.enums.LoanType;
import co.com.pragma.model.solicitudes.enums.Status;
import co.com.pragma.model.solicitudes.exception.UserNotFoundException;
import co.com.pragma.model.solicitudes.gateways.SolicitudesRepository;
import co.com.pragma.model.solicitudes.model.PageModel;
import co.com.pragma.model.solicitudes.model.SolicitudModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.verify;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class SolicitudeUseCaseTest {

    @Mock
    private SolicitudesRepository solicitudesRepository;

    @InjectMocks
    private SolicitudesUseCase solicitudesUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private SolicitudModel getSolicitudModelTest() {
        SolicitudModel model = new SolicitudModel();
        model.setId(1L);
        model.setUserDocumentNumber("123456789");
        model.setMount(new BigDecimal("5000.00"));
        model.setTermInMonths(12);
        model.setLoanType(Enum.valueOf(LoanType.class, "PERSONAL"));
        model.setStatus(Enum.valueOf(Status.class, "PENDING_REVIEW"));
        return model;
    }

    @Test
    void createSolicitud_Success() {
        SolicitudModel SolicitudModel = getSolicitudModelTest();

        when(solicitudesRepository.createSolicitud(any(SolicitudModel.class)))
                .thenReturn(Mono.just(SolicitudModel));

        StepVerifier.create(solicitudesUseCase.createSolicitud(SolicitudModel,null))
                .expectNext(SolicitudModel)
                .verifyComplete();

        verify(solicitudesRepository).createSolicitud(SolicitudModel);
    }

    @Test
    void createSolicitud_Failed() {
        SolicitudModel SolicitudModel = getSolicitudModelTest();

        when(solicitudesRepository.createSolicitud(any(SolicitudModel.class)))
                .thenReturn(Mono.error(new RuntimeException("Database error")));

        StepVerifier.create(solicitudesUseCase.createSolicitud(SolicitudModel,null))
                .expectErrorMatches(throwable ->
                        throwable instanceof RuntimeException &&
                                throwable.getMessage().equals("Database error")
                ).verify();

        verify(solicitudesRepository).createSolicitud(any(SolicitudModel.class));
    }


    @Test
    void createSolicitud_EntityNotFound() {
        SolicitudModel SolicitudModel = getSolicitudModelTest();

        when(solicitudesRepository.createSolicitud(any(SolicitudModel.class)))
                .thenReturn(Mono.error(new UserNotFoundException("Solicitud no encontrada")));

        StepVerifier.create(solicitudesUseCase.createSolicitud(SolicitudModel,null))
                .expectErrorMatches(throwable ->
                        throwable instanceof UserNotFoundException &&
                                throwable.getMessage().equals("Solicitud no encontrada"))
                .verify();

        verify(solicitudesRepository).createSolicitud(any(SolicitudModel.class));
    }

    @Test
    void findAllWithPagination_Success_WithResults() {
        SolicitudModel model = getSolicitudModelTest();
        String status = "PENDING_REVIEW";
        int page = 0;
        int size = 10;

        when(solicitudesRepository.findByStatusWithPagination(status, size, page * size))
                .thenReturn(Flux.just(model));
        when(solicitudesRepository.countByStatus(status))
                .thenReturn(Mono.just(1L));

        StepVerifier.create(solicitudesUseCase.findAllWithPagination(status, page, size))
                .assertNext(result -> {
                    List<SolicitudModel> data = (List<SolicitudModel>) result.get("data");
                    PageModel pageInfo = (PageModel) result.get("page");

                    assert data.size() == 1;
                    assert data.get(0).getId().equals(model.getId());

                    assert pageInfo.getTotalElements() == 1;
                    assert pageInfo.getCurrentPage() == 0;
                    assert pageInfo.getTotalPages() == 1;
                    assert !pageInfo.isHasPrevious();
                    assert !pageInfo.isHasNext();
                })
                .verifyComplete();

        verify(solicitudesRepository).findByStatusWithPagination(status, size, 0);
        verify(solicitudesRepository).countByStatus(status);
    }
}

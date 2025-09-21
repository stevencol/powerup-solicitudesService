package co.com.pragma.sqs.sender;

import co.com.pragma.model.solicitudes.gateways.SqsReporteRepository;
import co.com.pragma.model.solicitudes.model.SolicitudModel;
import com.google.gson.Gson;
import exceptions.SqsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import static constants.MessageExceptions.MSG_SQS_ENDEUDAMIENTO;

@Slf4j
@Component
public class SQSReporteAdapter implements SqsReporteRepository {
    private final SqsAsyncClient sqsClient;
    private final Gson gson;
    private final String queueUrl;

    public SQSReporteAdapter(SqsAsyncClient sqsClient, Gson gson, @Value("${aws.adapter.sqs.save.report.queueUrl}") String queueUrl) {
        this.sqsClient = sqsClient;
        this.gson = gson;
        this.queueUrl = queueUrl;
    }


    @Override
    public Mono<Void> sendSolicitud(SolicitudModel solicitudModel) {

        String solicitud = gson.toJson(solicitudModel);
        SendMessageRequest request = SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(solicitud)
                .build();


        return Mono.fromFuture(() -> sqsClient.sendMessage(request))
                .doOnNext(res ->
                        log.info("Solicitud enviado exitosamente {}", res.messageId()))
                .onErrorMap(ex -> {
                    log.error(MSG_SQS_ENDEUDAMIENTO, ex);
                    return new SqsException(String.format(MSG_SQS_ENDEUDAMIENTO, solicitudModel));
                }).then();

    }
}

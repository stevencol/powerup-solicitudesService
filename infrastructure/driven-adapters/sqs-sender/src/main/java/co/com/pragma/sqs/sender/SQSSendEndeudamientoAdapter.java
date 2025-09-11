package co.com.pragma.sqs.sender;

import co.com.pragma.model.solicitudes.gateways.SqsEndeudaminetoRepository;
import co.com.pragma.model.solicitudes.gateways.SqsSendRepository;
import co.com.pragma.model.solicitudes.model.sqs.SqsPlayLoadModel;
import com.google.gson.Gson;
import exceptions.SqsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import static constants.MessageExceptions.*;
import static constants.MessagesInfo.MSG_SQS_SUCCESS;

@Slf4j
@Component
public class SQSSendEndeudamientoAdapter implements SqsEndeudaminetoRepository {

    private final SqsAsyncClient sqsClient;
    private final Gson gson;
    private final String queueUrl;

    public SQSSendEndeudamientoAdapter(SqsAsyncClient sqsClient, Gson gson, @Value("${adapter.sqs2.queueUrl}") String queueUrl) {
        this.sqsClient = sqsClient;
        this.gson = gson;
        this.queueUrl = queueUrl;
    }

    @Override
    public Mono<Void> evaluate(SqsPlayLoadModel sqsPlayLoadModel) {
        String messageJson = gson.toJson(sqsPlayLoadModel);

        SendMessageRequest request = SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(messageJson)
                .build();

        return Mono.fromFuture(() -> sqsClient.sendMessage(request))
                .doOnNext(resp ->
                        log.info(MSG_SQS_SUCCESS)
                )
                .onErrorMap(ex -> {
                    log.error(MSG_SQS_ENDEUDAMIENTO, ex);
                    return new SqsException(String.format(MSG_SQS_ENDEUDAMIENTO, sqsPlayLoadModel.getSolicitudDto()));

                }).then();


    }

}




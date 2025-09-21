package co.com.pragma.sqs.sender;

import co.com.pragma.model.solicitudes.gateways.SqsSendRepository;
import co.com.pragma.model.solicitudes.model.NotificationMessageModel;
import com.google.gson.Gson;
import exceptions.SqsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import static constants.MessagesInfo.*;
import static constants.MessageExceptions.*;

@Slf4j
@Component
public class SQSSendSenderAdapter implements SqsSendRepository {

    private final SqsAsyncClient sqsClient;
    private final Gson gson;
    private final String queueUrl;

    public SQSSendSenderAdapter(SqsAsyncClient sqsClient, Gson gson, @Value("${aws.adapter.sqs.sender.status.queueUrl}") String queueUrl) {
        this.sqsClient = sqsClient;
        this.gson = gson;
        this.queueUrl = queueUrl;
    }

    @Override
    public Mono<Void> sendMessage(NotificationMessageModel message) {
        String messageJson = gson.toJson(message);


        SendMessageRequest request = SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(messageJson)
                .build();

        return Mono.fromFuture(() -> sqsClient.sendMessage(request))
                .doOnNext(resp ->
                        log.info(MSG_SQS_SUCCESS)
                )
                .onErrorMap(ex -> {
                    log.error(MSG_SQS_MESSAGE_ERROR, ex);
                    return new SqsException(String.format(MSG_SQS_MESSAGE_ERROR, message.getEmail()));

                }).then();


    }

}




package co.com.pragma.model.solicitudes.gateways;

import co.com.pragma.model.solicitudes.model.NotificationMessageModel;
import co.com.pragma.model.solicitudes.model.sqs.SqsPlayLoadModel;
import reactor.core.publisher.Mono;

public interface SqsEndeudaminetoRepository {

    Mono<Void> evaluate(SqsPlayLoadModel sqsPlayLoadModel);
}

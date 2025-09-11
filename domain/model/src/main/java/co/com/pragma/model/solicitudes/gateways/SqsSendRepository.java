package co.com.pragma.model.solicitudes.gateways;

import co.com.pragma.model.solicitudes.model.NotificationMessageModel;
import reactor.core.publisher.Mono;

public interface SqsSendRepository {

    Mono<Void> sendMessage(NotificationMessageModel message);
}

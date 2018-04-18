package org.humancellatlas.ingest.messaging;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.humancellatlas.ingest.messaging.model.SubmissionEnvelopeMessage;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Getter
@NoArgsConstructor
public class MessageSender {
    private @Autowired @NonNull RabbitMessagingTemplate rabbitMessagingTemplate;

    public void sendMessage(String exchange, String routingKey, SubmissionEnvelopeMessage payload){
        this.rabbitMessagingTemplate.convertAndSend(exchange, routingKey, payload);
    }
}

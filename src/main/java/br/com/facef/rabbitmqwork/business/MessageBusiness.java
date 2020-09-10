package br.com.facef.rabbitmqwork.business;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Message;

@Slf4j
public class MessageBusiness {
    public void processMessage(Message message) {
        log.info("Processing message: {}", message.toString());
        // Simulating exception and sending message to DLQ queue
        throw new AmqpRejectAndDontRequeueException("Business Rule Exception");
    }
}

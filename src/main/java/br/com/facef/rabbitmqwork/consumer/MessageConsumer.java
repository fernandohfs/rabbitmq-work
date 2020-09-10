package br.com.facef.rabbitmqwork.consumer;

import br.com.facef.rabbitmqwork.configuration.DirectExchangeConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class MessageConsumer {

    @RabbitListener(queues = DirectExchangeConfiguration.ORDER_MESSAGES_QUEUE_NAME)
    public void processOrderMessage(Message message) {
        log.info("Processing message: {}", message.toString());
        // Send message to DLQ queue
        throw new AmqpRejectAndDontRequeueException("Business Rule Exception");
    }
}

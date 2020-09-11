package br.com.facef.rabbitmqwork.consumer;

import br.com.facef.rabbitmqwork.business.MessageBusiness;
import br.com.facef.rabbitmqwork.configuration.DirectExchangeConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import static br.com.facef.rabbitmqwork.configuration.DirectExchangeConfiguration.DIRECT_EXCHANGE_NAME;
import static br.com.facef.rabbitmqwork.configuration.DirectExchangeConfiguration.ORDER_MESSAGES_QUEUE_PARKING_LOT_NAME;

@Configuration
@Slf4j
public class MessageConsumer {

    private static final Integer MAX_ATTEMPTS_TO_RETRY_PROCESS_DLQ_QUEUE = 3;

    @Autowired
    MessageBusiness messageBusiness;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = DirectExchangeConfiguration.ORDER_MESSAGES_QUEUE_NAME)
    public void processOrderMessage(Message message) {
        messageBusiness.processMessage(message);
    }

    @RabbitListener(queues = DirectExchangeConfiguration.ORDER_MESSAGES_QUEUE_DLQ_NAME)
    public void processOrderMessageDlq(Message message) {
        var maxAttempts = MAX_ATTEMPTS_TO_RETRY_PROCESS_DLQ_QUEUE;
        var attemps = 0;

        for (var i = 1; i <= maxAttempts; i++) {
            try {
                log.info("Trying to process message {} times. Message: {}", i, message.toString());
                messageBusiness.processMessageFromDlq(message);
            } catch (AmqpException ex) {
                log.info("Error to process message from DLQ. Message: {}", message.toString());
                attemps += 1;
            }
        }

        if (attemps == maxAttempts) {
            log.info("Sending message to ParkingLot. Message: {}", message.toString());
            try {
                this.rabbitTemplate.send(
                        DIRECT_EXCHANGE_NAME,
                        ORDER_MESSAGES_QUEUE_PARKING_LOT_NAME,
                        message);
            } catch (AmqpException ex) {
                log.info("Error on send message to ParkingLot. Message: {}", message.toString());
            }
        }
    }
}

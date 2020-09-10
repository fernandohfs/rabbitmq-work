package br.com.facef.rabbitmqwork.consumer;

import br.com.facef.rabbitmqwork.business.MessageBusiness;
import br.com.facef.rabbitmqwork.configuration.DirectExchangeConfiguration;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageConsumer {

    @Autowired
    MessageBusiness messageBusiness;

    @RabbitListener(queues = DirectExchangeConfiguration.ORDER_MESSAGES_QUEUE_NAME)
    public void processOrderMessage(Message message) {
        messageBusiness.processMessage(message);
    }
}

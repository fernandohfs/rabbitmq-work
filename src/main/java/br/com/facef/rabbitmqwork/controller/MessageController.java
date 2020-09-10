package br.com.facef.rabbitmqwork.controller;

import br.com.facef.rabbitmqwork.dto.MessageSend;
import br.com.facef.rabbitmqwork.producer.MessageProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired private MessageProducer messageProducer;

    @PostMapping("/send")
    public ResponseEntity sendMessageToDirectExchange(@RequestBody MessageSend messageSend) {
        messageProducer.sendFakeMessage();
        return ResponseEntity.accepted().build();
    }
}

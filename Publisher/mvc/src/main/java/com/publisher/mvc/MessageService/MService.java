package com.publisher.mvc.MessageService;

import com.publisher.mvc.Constant.Constant;
import com.publisher.mvc.MessageTemplate.Payment;
import com.publisher.mvc.MessageTemplate.Topup;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
public class MService {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Retryable(value = Exception.class, maxAttempts = 10, backoff = @Backoff(5000))
    public boolean sendMessageRequest(Object messageObject) {
        System.out.println("[*TRYING*] Trying to call services!");
        if (messageObject instanceof Payment) {
            rabbitTemplate.convertAndSend(Constant.EXCHANGE, Constant.ROUTING_KEYS[0], (Payment) messageObject);
        } else if (messageObject instanceof Topup) {
            rabbitTemplate.convertAndSend(Constant.EXCHANGE, Constant.ROUTING_KEYS[1], (Topup) messageObject);
        }
        return true;
    }

    @Recover
    public boolean recover() {
        return false;
    }
}

package com.example.springboottracer.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class SimpleListener {

    @RabbitListener(id = "id1", queues = "ccbd_queue")
    public void receiveEvent(String message) {
        log.info("inside receiveEvent {}", message);
    }

}

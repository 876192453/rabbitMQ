package com.bxy.rabbitmq;

import com.bxy.rabbitmq.config.RabbitmqConfig;
import javafx.application.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class Producer_SpringBoot {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void sendEmail(){
        String message="send email";
        rabbitTemplate.convertAndSend(RabbitmqConfig.EXCHANGE_TOPICS_INFORM,"inform.email",message);
    }
}

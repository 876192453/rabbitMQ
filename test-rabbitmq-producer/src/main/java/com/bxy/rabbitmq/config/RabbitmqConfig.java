package com.bxy.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


    //@RabbitListener(queues = {RabbitmqConfig.QUEUE_INFORM_EMAIL})
    @RabbitListener(queues = "#{rabbitmqConfig.QUEUE_INFORM_EMAIL}")

    @Configuration
    public class RabbitmqConfig {
        public static String QUEUE_INFORM_EMAIL = "queue_inform_email";
        public static String EXCHANGE_TOPICS_INFORM = "exchange_topics_inform";
        public static String ROUTINGKEY_EMAIL = "inform.#.email.#";

        //声明队列
        @Bean
        public Queue queueEmail() {
            return new Queue(QUEUE_INFORM_EMAIL);
        }

        //声明Topic交换机
        @Bean
        Exchange topicExchange() {
            return ExchangeBuilder.topicExchange(EXCHANGE_TOPICS_INFORM).durable(true).build();
        }

        //将队列与Topic交换机进行绑定，并指定路由键
        @Bean
        Binding topicBindingEmail(@Qualifier("queueEmail") Queue queue, @Qualifier("topicExchange")Exchange exchange) {
            return BindingBuilder.bind(queue).to(exchange).with(ROUTINGKEY_EMAIL).noargs();
        }

}

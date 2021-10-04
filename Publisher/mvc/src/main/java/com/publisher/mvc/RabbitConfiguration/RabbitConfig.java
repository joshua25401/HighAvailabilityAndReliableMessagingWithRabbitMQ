package com.publisher.mvc.RabbitConfiguration;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;

import com.publisher.mvc.Constant.Constant;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    // Make Queue Object
    @Bean
    Queue queuePayment() {
        return new Queue(Constant.QUEUE_NAMES[0], true); // Membuat queue dengan nama "payment_queue" dan durable = true
    }

    @Bean
    Queue queueTopUp() {
        return new Queue(Constant.QUEUE_NAMES[1], true); // Membuat queue dengan nama "topup_queue" dengan durable =
                                                         // true
    }

    // Membuat Exchange Object
    @Bean
    DirectExchange exchange() {
        return new DirectExchange(Constant.EXCHANGE);
    }

    // Membuat binding objek
    @Bean
    Binding bindingPaymentQueue(Queue queuePayment, DirectExchange directExchange) {
        return BindingBuilder.bind(queuePayment).to(directExchange).with(Constant.ROUTING_KEYS[0]);
    } // Membuat objek binding untuk ROUTING_KEY = "payments"

    @Bean
    Binding bindingTopUpQueue(Queue queueTopUp, DirectExchange directExchange) {
        return BindingBuilder.bind(queueTopUp).to(directExchange).with(Constant.ROUTING_KEYS[1]);
    }

    // Membuat POJO to JSON Converter
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // Make Connection Pool to Cluster (bcs we don't implement Load Balancer xD)
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setAddresses("localhost:5672,localhost:5673");
        connectionFactory.setChannelCacheSize(10);
        return connectionFactory;
    }

    // Membuat rabbit template
    @Bean
    public RabbitTemplate rabbitTemplate() {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}

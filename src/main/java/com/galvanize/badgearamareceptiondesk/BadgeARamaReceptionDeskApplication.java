package com.galvanize.badgearamareceptiondesk;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;

@SpringBootApplication
public class BadgeARamaReceptionDeskApplication implements RabbitListenerConfigurer {


    @Value("${amqp.exchange.name}")
    private String appExchangeName;

    @Value("${amqp.queue.name}")
    private String appQueueName;

    @Value("${amqp.routing.key}")
    private String appRoutingKey;

    public static void main(String[] args) {
        SpringApplication.run(BadgeARamaReceptionDeskApplication.class, args);
    }

    @Bean
    public TopicExchange getAppExchange() {
        return new TopicExchange(appExchangeName);
    }

    @Bean
    public Queue getAppQueue() {
        return new Queue(appQueueName);
    }

    @Bean
    public Binding declareBindingApp() {
        return BindingBuilder.bind(getAppQueue()).to(getAppExchange()).with(appRoutingKey);
    }

    @Bean
    public MappingJackson2MessageConverter consumerJackson2MessageConverter() {
        return new MappingJackson2MessageConverter();
    }

    @Bean
    public DefaultMessageHandlerMethodFactory messageHandlerMethodFactory() {
        DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
        factory.setMessageConverter(consumerJackson2MessageConverter());
        return factory;
    }

    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar registrar) {
        registrar.setMessageHandlerMethodFactory(messageHandlerMethodFactory());
    }
}

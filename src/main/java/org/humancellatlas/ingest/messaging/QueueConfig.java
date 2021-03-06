package org.humancellatlas.ingest.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;

/**
 * Created by rolando on 11/03/2018.
 */
@Configuration
public class QueueConfig implements RabbitListenerConfigurer  {

    @Bean
    TopicExchange stateTrackingExchange() { return new TopicExchange(Constants.Exchanges.STATE_TRACKING); }

    @Bean
    TopicExchange assayExchange() { return new TopicExchange(Constants.Exchanges.BUNDLE_EXCHANGE); }

    /* queues */

    @Bean
    Queue queueEnvelopeCreate() { return new Queue(Constants.Queues.ENVELOPE_CREATED, false); }

    @Bean
    Queue queueEnvelopeStateUpdate() { return new Queue(Constants.Queues.ENVELOPE_UPDATE, false); }

    @Bean
    Queue queueBundleableProcessSubmitted() { return new Queue(Constants.Queues.BUNDLEABLE_PROCESS_SUBMITTED, false); }

    @Bean
    Queue queueBundleableProcessCompleted() { return new Queue(Constants.Queues.BUNDLEABLE_PROCESS_COMPLETED, false); }

    /* queue bindings */

    @Bean
    Binding bindingEnvelopeCreate(Queue queueEnvelopeCreate, TopicExchange stateTrackingExchange) {
        return BindingBuilder.bind(queueEnvelopeCreate).to(stateTrackingExchange).with(Constants.RoutingKeys.ENVELOPE_CREATE);
    }

    @Bean
    Binding bindiEnvelopestateUpdate(Queue queueEnvelopeStateUpdate, TopicExchange stateTrackingExchange) {
        return BindingBuilder.bind(queueEnvelopeStateUpdate).to(stateTrackingExchange).with(Constants.RoutingKeys.ENVELOPE_STATE_UPDATE);
    }

    @Bean
    Binding bindingBundleableProcessSubmitted(Queue queueBundleableProcessSubmitted, TopicExchange assayExchange) {
        return BindingBuilder.bind(queueBundleableProcessSubmitted).to(assayExchange).with(Constants.RoutingKeys.BUNDLEABLE_PROCESS_SUBMITTED_ROUTING_KEY);
    }

    @Bean
    Binding bindingBundleableProcessCompleted(Queue queueBundleableProcessCompleted, TopicExchange assayExchange) {
        return BindingBuilder.bind(queueBundleableProcessCompleted).to(assayExchange).with(Constants.RoutingKeys.BUNDLEABLE_PROCESS_COMPLETED_ROUTING_KEY);
    }

    /* rabbit message config */

    @Bean
    public MessageConverter messageConverter() {
        return jackson2Converter();
    }

    @Bean
    public MappingJackson2MessageConverter jackson2Converter() {
        ObjectMapper mapper = new ObjectMapper();

        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        return new MappingJackson2MessageConverter();
    }

    @Bean
    public DefaultMessageHandlerMethodFactory myHandlerMethodFactory() {
        DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
        factory.setMessageConverter(jackson2Converter());
        return factory;
    }

    @Bean
    public Jackson2JsonMessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitMessagingTemplate rabbitMessagingTemplate(RabbitTemplate rabbitTemplate) {
        RabbitMessagingTemplate rmt = new RabbitMessagingTemplate(rabbitTemplate);
        rmt.setMessageConverter(this.jackson2Converter());
        return rmt;
    }

    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar registrar) {
        registrar.setMessageHandlerMethodFactory(myHandlerMethodFactory());
    }

}


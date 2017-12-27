package com.hedvig.asset_tracker.config;

import org.axonframework.amqp.eventhandling.spring.SpringAMQPMessageSource;
import org.axonframework.config.EventHandlingConfiguration;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.serialization.Serializer;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AxonConfig {

    @Value("${amqp.exchange.state}")
    private String stateExchange;

    @Value("${amqp.queue.state}")
    private String stateQueue;

    @Bean
    public EventBus eventBus(EventStorageEngine eventStorageEngine) {
        return new EmbeddedEventStore(eventStorageEngine);
    }

    @Bean
    public FanoutExchange assetStateExchange() {
        return new FanoutExchange(stateExchange, true, false);
    }

    @Bean
    public Queue assetStateQueue() {
        return new Queue(stateQueue, true, false, true);
    }

    @Bean
    public Binding assetStateBinding() {
        return new Binding(stateQueue, Binding.DestinationType.QUEUE, stateExchange, "", null);
    }

    @Bean
    public SpringAMQPMessageSource assetMessageSource(Serializer serializer) {
        return new AssetStateMessageSource(serializer);
    }

    @Autowired
    public void registerKioskEventProcessors(EventHandlingConfiguration config, SpringAMQPMessageSource assetMessageSource) {
        config.registerSubscribingEventProcessor("com.hedvig.asset_tracker.query.integration", c -> assetMessageSource);
    }
}

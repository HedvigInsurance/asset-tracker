package com.hedvig.asset_tracker.config;

import com.rabbitmq.client.Channel;
import org.axonframework.amqp.eventhandling.spring.SpringAMQPMessageSource;
import org.axonframework.serialization.Serializer;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

public class AssetStateMessageSource extends SpringAMQPMessageSource {

    public AssetStateMessageSource(Serializer serializer) {
        super(serializer);
    }

    @RabbitListener(queues = "${amqp.queue.state}")
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        super.onMessage(message, channel);
    }
}

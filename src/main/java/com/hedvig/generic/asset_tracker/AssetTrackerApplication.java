package com.hedvig.generic.asset_tracker;

import com.hedvig.generic.asset_tracker.externalEvents.KafkaProperties;
import org.axonframework.config.EventHandlingConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;


@SpringBootApplication
@EnableConfigurationProperties(KafkaProperties.class)
public class AssetTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AssetTrackerApplication.class, args);
    }

    @Autowired
    public void configure(EventHandlingConfiguration config) {
        config.usingTrackingProcessors();
    }
}

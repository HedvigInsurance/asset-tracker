package com.hedvig.asset_tracker;

import com.hedvig.asset_tracker.externalEvents.KafkaProperties;
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
        // Temporary change to make integration test work.
        // I want to handle event in command processing thread and check data in JPA repository.
        // Not perfect test, just to begin with.
        //config.usingTrackingProcessors();
    }
}

package com.hedvig.asset_tracker.config;

import com.hedvig.asset_tracker.aggregates.Asset;
import com.hedvig.asset_tracker.aggregates.AssetCommandHandler;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.commandhandling.gateway.DefaultCommandGateway;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.spring.config.AxonConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AxonConfig {
    private final AxonConfiguration axonConfiguration;

    private final EventBus eventBus;

    @Autowired
    public AxonConfig(AxonConfiguration axonConfiguration, EventBus eventBus) {
        this.axonConfiguration = axonConfiguration;
        this.eventBus = eventBus;
    }

    @Bean
    public AssetCommandHandler assetCommandHandler() {
        return new AssetCommandHandler(axonConfiguration.repository(Asset.class), eventBus);
    }

    @Autowired
    @Bean(name = "commandGateway")
    public CommandGateway commandGateway(CommandBus commandBus) {
        return new DefaultCommandGateway(commandBus);
    }
}

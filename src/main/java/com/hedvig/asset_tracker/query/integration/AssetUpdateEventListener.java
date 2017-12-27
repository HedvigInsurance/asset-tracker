package com.hedvig.asset_tracker.query.integration;

import com.hedvig.asset_tracker.query.AssetRepository;
import com.hedvig.common.events.AssetStateChangeEvent;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AssetUpdateEventListener {

    private final AssetRepository repository;

    @Autowired
    public AssetUpdateEventListener(AssetRepository repository) {
        this.repository = repository;
    }

    @EventHandler
    public void on(AssetStateChangeEvent event) {
        System.out.println(event.getId());
    }

}

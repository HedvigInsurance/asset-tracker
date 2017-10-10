package com.hedvig.generic.mustrename.query;

import com.hedvig.generic.mustrename.events.AssetCreatedEvent;
import com.hedvig.generic.mustrename.events.AssetDeletedEvent;
import com.hedvig.generic.mustrename.events.AssetUpdatedEvent;

import java.time.Instant;
import java.time.LocalDate;

import org.axonframework.eventhandling.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AssetEventListener {

	private static Logger log = LoggerFactory.getLogger(AssetEventListener.class);
    private final AssetRepository repository;

    @Autowired
    public AssetEventListener(AssetRepository userRepo) {
        this.repository = userRepo;
    }

    @EventHandler
    public void on(AssetCreatedEvent e){
        log.info("AssetCreatedEvent: " + e);
        AssetEntity asset = new AssetEntity();
        asset.id = e.getId();
        asset.userId = e.getUserId();
        asset.photoUrl = e.getPhotoUrl();
        asset.receiptUrl = e.getReceiptUrl();
        asset.title = e.getTitle();
        asset.state = e.getState();
        asset.registrationDate = e.getRegistrationDate();
        repository.save(asset);
    }
    
    @EventHandler
    public void on(AssetUpdatedEvent e){
        log.info("AssetUpdatedEvent: " + e);
        AssetEntity asset = repository.findById(e.getId()).orElseThrow(() -> new ResourceNotFoundException("Could not find memberchat."));
        asset.id = e.getId();
        asset.photoUrl = e.getPhotoUrl();
        asset.receiptUrl = e.getReceiptUrl();
        asset.title = e.getTitle();
        asset.state = e.getState();
        asset.includedInBasePackage = e.getIncludedInBasePackage();
        repository.save(asset);
    }
    
    @EventHandler
    public void on(AssetDeletedEvent e){
        log.info("AssetDeletedEvent: " + e);
        AssetEntity asset = new AssetEntity();
        asset.id = e.getId();
        repository.delete(asset);
    }
}

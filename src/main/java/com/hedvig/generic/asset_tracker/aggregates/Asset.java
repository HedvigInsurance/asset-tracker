package com.hedvig.generic.asset_tracker.aggregates;

import com.hedvig.generic.asset_tracker.events.AssetCreatedEvent;
import com.hedvig.generic.asset_tracker.events.AssetUpdatedEvent;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;

import java.time.LocalDate;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

@Aggregate
@Data
@Slf4j
public class Asset {
    @AggregateIdentifier
    private String id;

    private String photoUrl;

    private String receiptUrl;

    private String title;

    private String state;

    private Boolean includedInBasePackage;

    private String userId;

    private LocalDate registrationDate;

    @SuppressWarnings("UnusedDeclaration")
    public Asset() {
    }

    public Asset(
            String id,
            String photoUrl,
            String receiptUrl,
            String title,
            String state,
            Boolean includedInBasePackage,
            String userId,
            LocalDate registrationDate
    ) {
        apply(new AssetCreatedEvent(
                id, photoUrl, receiptUrl, title, state, includedInBasePackage, userId, registrationDate));
    }

    public void update(
            String id,
            String photoUrl,
            String receiptUrl,
            String title,
            String state,
            Boolean includedInBasePackage
    ) {
        apply(new AssetUpdatedEvent(
                id, photoUrl, receiptUrl, title, state, includedInBasePackage));
    }

    @EventSourcingHandler
    @SuppressWarnings("UnusedDeclaration")
    public void onAssetCreated(AssetCreatedEvent event) {
        this.id = event.getId();
        this.photoUrl = event.getPhotoUrl();
        this.receiptUrl = event.getReceiptUrl();
        this.title = event.getTitle();
        this.state = event.getState();
        this.includedInBasePackage = event.getIncludedInBasePackage();
        this.userId = event.getUserId();
        this.registrationDate = event.getRegistrationDate();
    }

    @EventSourcingHandler
    @SuppressWarnings("UnusedDeclaration")
    public void onAssetUpdated(AssetUpdatedEvent event) {
        this.photoUrl = event.getPhotoUrl();
        this.receiptUrl = event.getReceiptUrl();
        this.title = event.getTitle();
        this.state = event.getState();
        this.includedInBasePackage = event.getIncludedInBasePackage();
    }
}

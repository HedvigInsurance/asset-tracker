package com.hedvig.generic.asset_tracker.events;

import lombok.Value;
import org.axonframework.commandhandling.model.AggregateIdentifier;

import java.time.LocalDate;

@Value
public class AssetCreatedEvent {

    @AggregateIdentifier
    public String id;
    public String photoUrl;
    public String receiptUrl;
    public String title;
    public String state;
    public Boolean includedInBasePackage;
    public String userId;
    public LocalDate registrationDate;

    public AssetCreatedEvent(
            String id,
            String photoUrl,
            String receiptUrl,
            String title,
            String state,
            Boolean includedInBasePackage,
            String userId,
            LocalDate registrationDate) {
        this.id = id;
        this.photoUrl = photoUrl;
        this.receiptUrl = receiptUrl;
        this.title = title;
        this.state = state;
        this.includedInBasePackage = includedInBasePackage;
        this.userId = userId;
        this.registrationDate = registrationDate;
    }
}

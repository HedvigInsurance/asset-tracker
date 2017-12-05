package com.hedvig.generic.asset_tracker.events;

import lombok.Value;
import org.axonframework.commandhandling.model.AggregateIdentifier;

@Value
public class AssetUpdatedEvent {

    @AggregateIdentifier
    public String id;
    public String photoUrl;
    public String receiptUrl;
    public String title;
    public String state;
    public Boolean includedInBasePackage;

    public AssetUpdatedEvent(
            String id,
            String photoUrl,
            String receiptUrl,
            String title,
            String state,
            Boolean includedInBasePackage) {
        this.id = id;
        this.photoUrl = photoUrl;
        this.receiptUrl = receiptUrl;
        this.title = title;
        this.state = state;
        this.includedInBasePackage = includedInBasePackage;
    }
}

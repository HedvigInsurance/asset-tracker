package com.hedvig.asset_tracker.events;

import lombok.Value;
import org.axonframework.commandhandling.model.AggregateIdentifier;

@Value
public class AssetUpdatedEvent {

    @AggregateIdentifier
    String id;

    String photoUrl;

    String receiptUrl;

    String title;

    String state;

    Boolean includedInBasePackage;
}

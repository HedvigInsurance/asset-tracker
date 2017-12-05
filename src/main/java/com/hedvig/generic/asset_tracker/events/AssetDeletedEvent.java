package com.hedvig.generic.asset_tracker.events;

import lombok.Value;
import org.axonframework.commandhandling.model.AggregateIdentifier;

@Value
public class AssetDeletedEvent {

    @AggregateIdentifier
    public String id;

    public AssetDeletedEvent(
            String id
    ) {
        this.id = id;

    }
}

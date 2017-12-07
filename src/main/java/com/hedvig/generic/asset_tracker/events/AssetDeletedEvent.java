package com.hedvig.generic.asset_tracker.events;

import lombok.Value;
import org.axonframework.commandhandling.model.AggregateIdentifier;

@Value
public class AssetDeletedEvent {

    @AggregateIdentifier
    String id;
}

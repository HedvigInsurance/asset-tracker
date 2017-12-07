package com.hedvig.generic.asset_tracker.events;

import lombok.Value;
import org.axonframework.commandhandling.model.AggregateIdentifier;

import java.time.LocalDate;

@Value
public class AssetCreatedEvent {
    @AggregateIdentifier
    String id;

    String photoUrl;

    String receiptUrl;

    String title;

    String state;

    Boolean includedInBasePackage;

    String userId;

    LocalDate registrationDate;
}

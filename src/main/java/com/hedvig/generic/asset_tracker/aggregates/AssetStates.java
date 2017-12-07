package com.hedvig.generic.asset_tracker.aggregates;

public enum AssetStates {
    CREATED,
    PENDING,
    WAITING_FOR_PAYMENT,
    NOT_COVERED,
    COVERED,
    DELETED
}

package com.hedvig.generic.asset_tracker.commands;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.TargetAggregateIdentifier;

@Slf4j
@Value
public class DeleteAssetCommand {

    @TargetAggregateIdentifier
    String id;
}

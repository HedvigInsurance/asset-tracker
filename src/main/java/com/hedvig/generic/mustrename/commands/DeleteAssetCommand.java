package com.hedvig.generic.mustrename.commands;

import lombok.Value;
import org.axonframework.commandhandling.TargetAggregateIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Value
public class DeleteAssetCommand {

    private static Logger log = LoggerFactory.getLogger(DeleteAssetCommand.class);

    @TargetAggregateIdentifier
    public String id;

    public DeleteAssetCommand(String id) {
        log.info("DeleteAssetCommand");
        this.id = id;
    }
}

package com.hedvig.generic.mustrename.commands;

import com.hedvig.generic.mustrename.web.dto.AssetDTO;
import lombok.Value;
import org.axonframework.commandhandling.TargetAggregateIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Value
public class CreateAssetCommand {

    private static Logger log = LoggerFactory.getLogger(CreateAssetCommand.class);

    @TargetAggregateIdentifier
    public String id;
    public String photoUrl;
    public String receiptUrl;
    public String title;
    public String state;
    public Boolean includedInBasePackage;
    public String userId;

    public CreateAssetCommand(String userId, String id, AssetDTO asset) {
        log.info("CreateAssetCommand");
        this.userId = userId;
        this.id = id;
        this.photoUrl = asset.photoUrl;
        this.receiptUrl = asset.receiptUrl;
        this.title = asset.title;
        this.state = asset.state;
        this.includedInBasePackage = asset.includedInBasePackage;
        log.info(this.toString());
    }
}

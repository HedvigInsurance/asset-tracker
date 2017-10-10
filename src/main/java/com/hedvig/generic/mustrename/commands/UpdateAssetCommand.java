package com.hedvig.generic.mustrename.commands;

import lombok.Value;
import org.axonframework.commandhandling.TargetAggregateIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hedvig.generic.mustrename.query.AssetEventListener;
import com.hedvig.generic.mustrename.web.dto.AssetDTO;

import java.time.LocalDate;

@Value
public class UpdateAssetCommand {

	private static Logger log = LoggerFactory.getLogger(UpdateAssetCommand.class);
	
    @TargetAggregateIdentifier
    public String id;
    public String photoUrl;
    public String receiptUrl;
    public String title;
    public String state;
    public Boolean includedInBasePackage;

	public UpdateAssetCommand(AssetDTO asset) {
        log.info("UpdateAssetCommand");
        this.id = asset.id;
        this.photoUrl = asset.photoUrl;
        this.receiptUrl = asset.receiptUrl;
        this.title = asset.title;
        this.state = asset.state;
        this.includedInBasePackage = asset.includedInBasePackage;
        log.info(this.toString());
	}
}

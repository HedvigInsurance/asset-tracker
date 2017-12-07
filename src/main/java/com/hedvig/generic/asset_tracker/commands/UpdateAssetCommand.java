package com.hedvig.generic.asset_tracker.commands;

import com.hedvig.generic.asset_tracker.web.dto.AssetDTO;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.TargetAggregateIdentifier;

@Slf4j
@Value
public class UpdateAssetCommand {

    @TargetAggregateIdentifier
    String id;

    String photoUrl;

    String receiptUrl;

    String title;

    String state;

    Boolean includedInBasePackage;

    public static UpdateAssetCommand fromDTO(AssetDTO assetDTO) {
        log.info("Created CreateAssetCommand from DTO {}", assetDTO);
        return new UpdateAssetCommand(
                assetDTO.getId(),
                assetDTO.getPhotoUrl(),
                assetDTO.getReceiptUrl(),
                assetDTO.getTitle(),
                assetDTO.getState(),
                assetDTO.getIncludedInBasePackage());
    }
}

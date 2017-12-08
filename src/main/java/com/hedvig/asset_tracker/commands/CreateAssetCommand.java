package com.hedvig.asset_tracker.commands;

import com.hedvig.asset_tracker.web.dto.AssetDTO;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.TargetAggregateIdentifier;

@Slf4j
@Value
public class CreateAssetCommand {

    @TargetAggregateIdentifier
    String id;

    String photoUrl;

    String receiptUrl;

    String title;

    String state;

    Boolean includedInBasePackage;

    String userId;

    public static CreateAssetCommand fromDTO(String userId, String id, AssetDTO assetDTO) {
        log.info("Created CreateAssetCommand from DTO {} for asset id {} and user id {}", assetDTO, id, userId);
        return new CreateAssetCommand(
                id,
                assetDTO.getPhotoUrl(),
                assetDTO.getReceiptUrl(),
                assetDTO.getTitle(),
                assetDTO.getState(),
                assetDTO.getIncludedInBasePackage(),
                userId);
    }
}

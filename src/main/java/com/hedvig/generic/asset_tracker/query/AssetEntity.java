package com.hedvig.generic.asset_tracker.query;

import com.hedvig.generic.asset_tracker.web.dto.AssetDTO;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
@Slf4j
public class AssetEntity {

    @Id
    public String id;

    public String photoUrl;

    public String receiptUrl;

    public String title;

    public String state;

    public Boolean includedInBasePackage;

    public String userId;

    public LocalDate registrationDate;

    public AssetDTO convertToDTO() {
        return new AssetDTO(
                id,
                photoUrl,
                receiptUrl,
                title,
                state,
                includedInBasePackage,
                registrationDate);
    }
}

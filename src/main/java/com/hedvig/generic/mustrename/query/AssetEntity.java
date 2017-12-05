package com.hedvig.generic.mustrename.query;

import com.hedvig.generic.mustrename.web.dto.AssetDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
public class AssetEntity {

    private static Logger log = LoggerFactory.getLogger(AssetEntity.class);

    @Id
    public String id;
    public String photoUrl;
    public String receiptUrl;
    public String title;
    public String state;
    public boolean includedInBasePackage;
    public String userId;
    public LocalDate registrationDate;

    public AssetDTO convertToDTO() {
        AssetDTO asset = new AssetDTO();
        asset.id = id;
        asset.photoUrl = photoUrl;
        asset.receiptUrl = receiptUrl;
        asset.title = title;
        asset.state = state;
        asset.includedInBasePackage = includedInBasePackage;
        asset.registrationDate = registrationDate;
        return asset;
    }
}

package com.hedvig.generic.asset_tracker.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Value;

import java.time.LocalDate;

@Value
public class AssetDTO {

    String id;

    String photoUrl;

    String receiptUrl;

    String title;

    String state;

    Boolean includedInBasePackage;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate registrationDate;
}

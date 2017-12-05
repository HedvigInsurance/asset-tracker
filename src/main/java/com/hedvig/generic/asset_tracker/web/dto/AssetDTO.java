package com.hedvig.generic.asset_tracker.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@NoArgsConstructor
@ToString
public class AssetDTO {

    public String id;
    public String photoUrl;
    public String receiptUrl;
    public String title;
    public String state;
    public Boolean includedInBasePackage;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    public LocalDate registrationDate;
}

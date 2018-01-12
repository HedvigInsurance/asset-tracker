package com.hedvig.generic.mustrename.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class AssetDTO {

    public String id;
    public String photoUrl;
    public String receiptUrl;
    public String title;
    public String state;
    public Boolean includedInBasePackage;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    public LocalDate registrationDate;

    public AssetDTO() {
    }

    /*public AssetDTO(String id, String name, LocalDate registrationDate) {
        this.id = id;
        this.registrationDate = registrationDate;
    }*/
}

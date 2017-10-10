package com.hedvig.generic.mustrename.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;

import java.time.LocalDate;

public class AssetDTO {

    public String id;
    public String photoUrl;
    public String receiptUrl;
    public String title;
    public String state;
    public Boolean includedInBasePackage;

    @JsonDeserialize(using= LocalDateDeserializer.class)
    public LocalDate registrationDate;

    public AssetDTO(){}

    /*public AssetDTO(String id, String name, LocalDate registrationDate) {
        this.id = id;
        this.registrationDate = registrationDate;
    }*/
}

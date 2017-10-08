package com.hedvig.generic.mustrename.query;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
public class AssetEntity {

    @Id
    public String id;

    public String name;

    public LocalDate registrationDate;


}

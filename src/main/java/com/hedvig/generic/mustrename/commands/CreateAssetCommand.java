package com.hedvig.generic.mustrename.commands;

import lombok.Value;
import org.axonframework.commandhandling.TargetAggregateIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hedvig.generic.mustrename.query.AssetEventListener;

import java.time.LocalDate;

@Value
public class CreateAssetCommand {

	private static Logger log = LoggerFactory.getLogger(CreateAssetCommand.class);
	
    @TargetAggregateIdentifier
    public String id;
    private String name;
    private LocalDate registrationDate;

    public CreateAssetCommand(String id, String name, LocalDate registrationDate) {
        log.info("CreateAssetCommand");
        this.id = id;
        this.name = name;
        this.registrationDate = registrationDate;
        log.info(this.toString());
    } 
}

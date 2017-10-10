package com.hedvig.generic.mustrename.events;

import lombok.Value;
import org.axonframework.commandhandling.model.AggregateIdentifier;

import java.time.LocalDate;

@Value
public class AssetDeletedEvent {

	@AggregateIdentifier
    public String id;

	public AssetDeletedEvent(    
			String id
			) {
		this.id = id; 

	}
}

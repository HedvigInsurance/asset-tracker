package com.hedvig.asset_tracker.aggregates;

import com.hedvig.asset_tracker.commands.CreateAssetCommand;
import com.hedvig.asset_tracker.commands.DeleteAssetCommand;
import com.hedvig.asset_tracker.commands.UpdateAssetCommand;
import com.hedvig.asset_tracker.events.AssetCreatedEvent;
import com.hedvig.asset_tracker.events.AssetDeletedEvent;
import com.hedvig.asset_tracker.events.AssetUpdatedEvent;
import com.hedvig.common.constant.AssetState;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;

import java.time.LocalDate;

import static org.axonframework.commandhandling.model.AggregateLifecycle.*;

@Aggregate
@NoArgsConstructor
public class AssetAggregate {

    @AggregateIdentifier
    private String id;

    @CommandHandler
    public AssetAggregate(CreateAssetCommand command) {
        apply(new AssetCreatedEvent(
                command.getId(),
                command.getPhotoUrl(),
                command.getReceiptUrl(),
                command.getTitle(),
                command.getState(),
                command.getIncludedInBasePackage(),
                command.getUserId(),
                LocalDate.now()
        ));
    }

    @CommandHandler
    public void update(UpdateAssetCommand command) {
        apply(new AssetUpdatedEvent(command.getId(),
                command.getPhotoUrl(),
                command.getReceiptUrl(),
                command.getTitle(),
                AssetState.PENDING.toString(),
                command.getIncludedInBasePackage()));
    }

    @CommandHandler
    public void delete(DeleteAssetCommand command) {
        apply(new AssetDeletedEvent(command.getId()));
        markDeleted();
    }

    @EventSourcingHandler
    public void on(AssetCreatedEvent event) {
        this.id = event.getId();
    }

}

package com.hedvig.generic.asset_tracker.aggregates;

import com.hedvig.generic.asset_tracker.commands.CreateAssetCommand;
import com.hedvig.generic.asset_tracker.commands.DeleteAssetCommand;
import com.hedvig.generic.asset_tracker.commands.UpdateAssetCommand;
import com.hedvig.generic.asset_tracker.events.AssetCreatedEvent;
import com.hedvig.generic.asset_tracker.events.AssetDeletedEvent;
import com.hedvig.generic.asset_tracker.events.AssetUpdatedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

/**
 * This is an example Aggregate and should be remodeled to suit the needs of you domain.
 */
@Aggregate
public class AssetAggregate {

    private static Logger log = LoggerFactory.getLogger(AssetAggregate.class);

    @AggregateIdentifier
    public String id;

    public String photoUrl;

    public String receiptUrl;

    public String title;

    public String state;

    public Boolean includedInBasePackage;

    public String userId;

    public LocalDate registrationDate;

    public AssetAggregate() {
        log.info("Instansiating AssetAggregate");
    }

    @CommandHandler
    public AssetAggregate(CreateAssetCommand command) {
        log.info("create");
        AssetCreatedEvent e = new AssetCreatedEvent(
                command.getId(),
                command.getPhotoUrl(),
                command.getReceiptUrl(),
                command.getTitle(),
                AssetStates.PENDING.toString(), // Default set to pending
                command.getIncludedInBasePackage(),
                command.getUserId(),
                LocalDate.now()); // Registration time

        apply(e);
    }

    @CommandHandler
    public void update(UpdateAssetCommand command) {
        log.info("update");
        AssetUpdatedEvent e = new AssetUpdatedEvent(
                command.getId(),
                command.getPhotoUrl(),
                command.getReceiptUrl(),
                command.getTitle(),
                command.getState(),
                command.getIncludedInBasePackage());

        apply(e);
    }

    @CommandHandler
    public void delete(DeleteAssetCommand command) {
        log.info("delete");
        apply(new AssetDeletedEvent(command.getId()));
    }

    @EventSourcingHandler
    public void on(AssetCreatedEvent e) {
        this.id = e.getId();
        this.photoUrl = e.getPhotoUrl();
        this.receiptUrl = e.getReceiptUrl();
        this.title = e.getTitle();
        this.state = e.getState();
        this.includedInBasePackage = e.getIncludedInBasePackage();
        this.userId = e.getUserId();
        this.registrationDate = e.getRegistrationDate();
    }

}

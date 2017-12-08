package com.hedvig.asset_tracker.aggregates;

import com.hedvig.asset_tracker.commands.CreateAssetCommand;
import com.hedvig.asset_tracker.commands.DeleteAssetCommand;
import com.hedvig.asset_tracker.commands.UpdateAssetCommand;
import com.hedvig.asset_tracker.events.AssetDeletedEvent;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateNotFoundException;
import org.axonframework.commandhandling.model.Repository;
import org.axonframework.eventhandling.EventBus;

import java.time.LocalDate;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

@Slf4j
public class AssetCommandHandler {

    private final EventBus eventBus;

    private Repository<Asset> repository;

    public AssetCommandHandler(Repository<Asset> repository, EventBus eventBus) {
        this.repository = repository;
        this.eventBus = eventBus;
    }

    @CommandHandler
    @SuppressWarnings("UnusedDeclaration")
    public void create(CreateAssetCommand command) throws Exception {
        log.info("Handle CreateAssetCommand {}", command);

        repository.newInstance(() -> new Asset(
                command.getId(),
                command.getPhotoUrl(),
                command.getReceiptUrl(),
                command.getTitle(),
                AssetStates.PENDING.toString(),
                command.getIncludedInBasePackage(),
                command.getUserId(),
                LocalDate.now()
        ));
    }

    @CommandHandler
    @SuppressWarnings("UnusedDeclaration")
    public void update(UpdateAssetCommand command) {
        log.info("Handle UpdateAssetCommand {}", command);
        try {
            val assetAggregate = repository.load(command.getId());
            assetAggregate.execute(asset -> asset.update(
                    command.getId(),
                    command.getPhotoUrl(),
                    command.getReceiptUrl(),
                    command.getTitle(),
                    AssetStates.PENDING.toString(),
                    command.getIncludedInBasePackage()));
        } catch (AggregateNotFoundException exception) {
            log.error("Aggregate not found for UpdateAssetCommand {}", command);
        }
    }

    @CommandHandler
    @SuppressWarnings("UnusedDeclaration")
    public void delete(DeleteAssetCommand command) {
        log.info("delete");
        apply(new AssetDeletedEvent(command.getId()));
    }
}

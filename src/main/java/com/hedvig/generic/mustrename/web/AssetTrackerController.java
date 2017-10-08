package com.hedvig.generic.mustrename.web;

import com.hedvig.generic.mustrename.aggregates.AssetAggregate;
import com.hedvig.generic.mustrename.commands.CreateAssetCommand;
import com.hedvig.generic.mustrename.commands.DeleteAssetCommand;
import com.hedvig.generic.mustrename.query.AssetRepository;
import com.hedvig.generic.mustrename.web.dto.AssetDTO;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.commandhandling.gateway.DefaultCommandGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
public class AssetTrackerController {

	private static Logger log = LoggerFactory.getLogger(AssetTrackerController.class);
    private final AssetRepository userRepository;
    private final CommandGateway commandBus;

    @Autowired
    public AssetTrackerController(CommandBus commandBus, AssetRepository repository) {
        this.commandBus = new DefaultCommandGateway(commandBus);
        this.userRepository = repository;
    }

    // Based CRUD commands for assets ---------------------- //
    
    @RequestMapping(path="/asset/{userId}", method = RequestMethod.GET)
    public ResponseEntity<AssetDTO> getAsset(@PathVariable String userId) {
        return userRepository
                .findById(userId)
                .map(u -> ResponseEntity.ok(new AssetDTO(u.id, u.name, u.registrationDate)))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(path = "/asset/", method = RequestMethod.POST)
    public ResponseEntity<?> createAsset(@RequestBody AssetDTO user) {
        UUID uid = UUID.randomUUID();
        log.info(uid.toString());
        commandBus.sendAndWait(new CreateAssetCommand(uid.toString(), user.name, user.registrationDate));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(uid.toString()).toUri();
        return ResponseEntity.created(location).build();
    }

    @RequestMapping(path = "/asset/", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteAsset(@RequestBody AssetDTO asset) {
        log.info("Deleting:" + asset.id);
        commandBus.sendAndWait(new DeleteAssetCommand(asset.id, asset.name, asset.registrationDate));
        return ResponseEntity.ok("Deleted asset:" + asset.id);
    }
    
    @RequestMapping(path = "/asset/", method = RequestMethod.PUT)
    public ResponseEntity<?> updateAsset(@RequestBody AssetDTO asset) {
        log.info("Updating:" + asset.id);
        commandBus.sendAndWait(new DeleteAssetCommand(asset.id, asset.name, asset.registrationDate));
        return ResponseEntity.ok("Updated asset:" + asset.id);
    }
    
}

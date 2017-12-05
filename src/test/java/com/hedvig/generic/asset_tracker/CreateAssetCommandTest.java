package com.hedvig.generic.asset_tracker;

import com.hedvig.generic.asset_tracker.aggregates.AssetStates;
import com.hedvig.generic.asset_tracker.commands.CreateAssetCommand;
import com.hedvig.generic.asset_tracker.web.dto.AssetDTO;
import lombok.val;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CreateAssetCommandTest {
    @Autowired
    private CommandGateway commandGateway;

    @Test
    public void createAssetCommandTest() {
        val hid = "user-token-001";
        val asset = new AssetDTO();
        asset.includedInBasePackage = true;
        asset.photoUrl = "photo-url-what-should-be-here";
        asset.receiptUrl = null;
        asset.state = AssetStates.PENDING.toString();
        asset.title = "My first test asset";
        val uid = UUID.randomUUID();

        commandGateway.sendAndWait(new CreateAssetCommand(hid, uid.toString(), asset));
    }
}

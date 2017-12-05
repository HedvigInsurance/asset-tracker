package com.hedvig.generic.asset_tracker;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hedvig.generic.asset_tracker.aggregates.AssetStates;
import com.hedvig.generic.asset_tracker.web.dto.AssetDTO;
import lombok.val;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.nio.charset.Charset;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
@RunWith(SpringRunner.class)
public class AssetTrackerControllerTests {
    private static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8")
    );

    @Autowired
    private MockMvc mockMvc;


    @Test
    public void createAssetTest() throws Exception {

        mockMvc.perform(
                post("/asset")
                        .header("hedvig.token", "user-token-001")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(convertObjectToJsonBytes(testAsset())))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", notNullValue()));
    }

    private AssetDTO testAsset() {
        val asset = new AssetDTO();
        asset.includedInBasePackage = true;
        asset.photoUrl = "photo-url-what-should-be-here";
        asset.receiptUrl = null;
        asset.state = AssetStates.PENDING.toString();
        asset.title = "My first test asset";
        return asset;
    }

    private byte[] convertObjectToJsonBytes(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsBytes(object);
    }
}

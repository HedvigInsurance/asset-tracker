package com.hedvig.asset_tracker.web;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hedvig.asset_tracker.query.AssetRepository;
import com.hedvig.asset_tracker.web.dto.AssetCreatedDTO;
import com.hedvig.asset_tracker.web.dto.AssetDTO;
import com.hedvig.common.constant.AssetState;
import lombok.val;
import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore;
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
import java.util.UUID;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
@RunWith(SpringRunner.class)
public class AssetTrackerControllerTests {

    private static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private EmbeddedEventStore embeddedEventStore;

    @Test
    public void createAssetTest() throws Exception {
        val userId = "user-token-001";
        val assetDTO = testAsset();
        val json = serialize(assetDTO);
        val jsonString = new String(json);

        val createResult = mockMvc.perform(
                post("/asset")
                        .header("hedvig.token", userId)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(json))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andReturn();
        val createDTO = deserialize(createResult.getResponse().getContentAsByteArray(), AssetCreatedDTO.class);
        val createdId = createDTO.getId();

        val createdAsset = assetRepository.findById(createdId);

        embeddedEventStore.readEvents(createdId).asStream().forEach(e -> {
            System.out.println(e.getPayload().toString());
        });

        assertTrue(createdAsset.isPresent());
        assertEquals(createDTO.getId(), createdAsset.get().id);
        assertEquals(assetDTO.getIncludedInBasePackage(), createdAsset.get().includedInBasePackage);
        assertEquals(assetDTO.getPhotoUrl(), createdAsset.get().photoUrl);
        assertEquals(assetDTO.getReceiptUrl(), createdAsset.get().receiptUrl);
        assertEquals(assetDTO.getTitle(), createdAsset.get().title);
        assertEquals(userId, createdAsset.get().userId);
    }

    @Test
    public void readAssetTest() throws Exception {
        mockMvc.perform(
                get("/asset")
                        .header("hedvig.token", "user-token-001")
                        .contentType(APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8));
    }

    private AssetDTO testAsset() {
        val uid = UUID.randomUUID().toString();
        return new AssetDTO(
                uid,
                "photo-url-what-should-be-here",
                "receipt-url",
                "My first test asset",
                AssetState.PENDING.toString(),
                true,
                null);
    }

    private byte[] serialize(Object object) throws IOException {
        val mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsBytes(object);
    }

    private <T extends Object> T deserialize(byte[] src, Class<T> type) throws IOException {
        val mapper = new ObjectMapper();
        return mapper.readValue(src, type);
    }
}

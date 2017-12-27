package com.hedvig.asset_tracker.web;

import com.hedvig.asset_tracker.commands.CreateAssetCommand;
import com.hedvig.asset_tracker.commands.DeleteAssetCommand;
import com.hedvig.asset_tracker.commands.UpdateAssetCommand;
import com.hedvig.asset_tracker.query.AssetRepository;
import com.hedvig.asset_tracker.query.FileUploadRepository;
import com.hedvig.asset_tracker.query.UploadFile;
import com.hedvig.asset_tracker.web.dto.AssetCreatedDTO;
import com.hedvig.asset_tracker.web.dto.AssetDTO;
import lombok.val;
import org.apache.commons.compress.utils.IOUtils;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.commandhandling.gateway.DefaultCommandGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class AssetTrackerController {

    private static Logger log = LoggerFactory.getLogger(AssetTrackerController.class);
    private final AssetRepository userRepository;
    private final CommandGateway commandBus;
    private final FileUploadRepository filerepo;
    private final String imageBaseUrl = "${hedvig.product-pricing.url}";

    @Autowired
    public AssetTrackerController(CommandBus commandBus, AssetRepository repository, FileUploadRepository filerepo) {
        this.commandBus = new DefaultCommandGateway(commandBus);
        this.userRepository = repository;
        this.filerepo = filerepo;
    }

    @RequestMapping(value = "/asset/fileupload", method = RequestMethod.POST, produces = "application/json;charset=UTF-8", consumes = "multipart/form-data")
    public ResponseEntity<String> handleFileUpload(@ModelAttribute("file") MultipartFile fileUpload,
                                                   @RequestHeader(value = "hedvig.token", required = false) String hid) throws Exception {
        UUID uid = UUID.randomUUID();
        log.info("Saving file: " + fileUpload.getOriginalFilename());

        UploadFile uploadFile = new UploadFile();
        uploadFile.setFileName(fileUpload.getOriginalFilename());
        uploadFile.setData(fileUpload.getBytes());
        uploadFile.setUserId(hid);
        uploadFile.setImageId(uid);
        uploadFile.setContentType(fileUpload.getContentType());
        filerepo.save(uploadFile);

        return ResponseEntity.noContent().header("Location", imageBaseUrl + "/asset/image/" + uid.toString()).build();
    }

    @RequestMapping(value = "/asset/image/{image_id}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getImageAsResponseEntity(@PathVariable UUID image_id,
                                                           @RequestHeader(value = "hedvig.token", required = false) String hid) {
        HttpHeaders headers = new HttpHeaders();

        Optional<UploadFile> file = filerepo.findByImageId(image_id);
        if (file.isPresent()) {
            InputStream in = new ByteArrayInputStream(file.get().getData());
            byte[] media;
            try {
                media = IOUtils.toByteArray(in);
                log.info("Get image:" + image_id + " content-type:" + file.get().getContentType());

                headers.setContentType(MediaType.valueOf(file.get().getContentType()));
                headers.setCacheControl(CacheControl.noCache().getHeaderValue());

                return new ResponseEntity<>(media, headers, HttpStatus.OK);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    // Based CRUD commands for assets ---------------------- //

    @RequestMapping(path = "/asset", method = RequestMethod.GET)
    public List<AssetDTO> getAsset(@RequestHeader(value = "hedvig.token", required = false) String hid) {
        log.info("Finding all assets for user: " + hid);
        return userRepository.findByUserId(hid).stream().map(n -> n.convertToDTO()).collect(Collectors.toList());
    }

    @RequestMapping(path = "/asset", method = RequestMethod.POST)
    public ResponseEntity<AssetCreatedDTO> createAsset(
            @RequestBody AssetDTO asset,
            @RequestHeader(value = "hedvig.token", required = false) String hid) {
        val uid = UUID.randomUUID();
        log.info("Creating asset uid: {}, data: {}", uid, asset);
        commandBus.sendAndWait(CreateAssetCommand.fromDTO(hid, uid.toString(), asset));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(uid.toString()).toUri();
        val assetCreated = new AssetCreatedDTO(uid.toString());
        return ResponseEntity.created(location).body(assetCreated);
    }

    @RequestMapping(path = "/asset/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteAsset(@PathVariable String id) {
        log.info("Deleting:" + id);
        commandBus.sendAndWait(new DeleteAssetCommand(id));
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(path = "/asset/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateAsset(@RequestBody AssetDTO asset, @PathVariable String id) {
        if (!id.equals(asset.getId()))
            return ResponseEntity.badRequest().body("{\"message\":\"Idnumber in body does not match resource id.\"}");

        log.info("Updating asset {}", asset.getId());
        commandBus.sendAndWait(UpdateAssetCommand.fromDTO(asset));
        return ResponseEntity.ok("{\"id:\":\"" + asset.getId() + "\"}");
    }

}
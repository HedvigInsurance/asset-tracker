package com.hedvig.generic.mustrename.web;

import com.hedvig.generic.mustrename.commands.CreateAssetCommand;
import com.hedvig.generic.mustrename.commands.DeleteAssetCommand;
import com.hedvig.generic.mustrename.commands.UpdateAssetCommand;
import com.hedvig.generic.mustrename.query.AssetEntity;
import com.hedvig.generic.mustrename.query.AssetRepository;
import com.hedvig.generic.mustrename.query.FileUploadRepository;
import com.hedvig.generic.mustrename.query.UploadFile;
import com.hedvig.generic.mustrename.web.dto.AssetDTO;
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
    private final AssetRepository assetRepository;
    private final CommandGateway commandBus;
    private final FileUploadRepository filerepo;
    private final String imageBaseUrl = "${hedvig.product-pricing.url}";

    @Autowired
    public AssetTrackerController(CommandBus commandBus, AssetRepository repository,
                                  FileUploadRepository filerepo, AssetRepository assetRepository) {
        this.commandBus = new DefaultCommandGateway(commandBus);
        this.userRepository = repository;
        this.filerepo = filerepo;
        this.assetRepository = assetRepository;
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
        /*return userRepository
                .findByUserId(hid)
                .map(u -> ResponseEntity.ok(new AssetDTO(hid, u.id, u.name, u.registrationDate)))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));*/
        log.info("Finding all assets for user: " + hid);
        return (List<AssetDTO>) userRepository.findByUserId(hid).stream().map(n -> n.convertToDTO()).collect(Collectors.toList());

    }

    @RequestMapping(path = "/asset", method = RequestMethod.POST)
    public ResponseEntity<?> createAsset(@RequestBody AssetDTO asset, @RequestHeader(value = "hedvig.token", required = false) String hid) {
        UUID uid = UUID.randomUUID();
        log.info(uid.toString());
        commandBus.sendAndWait(new CreateAssetCommand(hid, uid.toString(), asset));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(uid.toString()).toUri();
        return ResponseEntity.created(location).body("{\"id:\":\"" + uid.toString() + "\"}");
    }

    @RequestMapping(path = "/asset/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteAsset(@PathVariable String id) {
        log.info("Deleting:" + id);
        commandBus.sendAndWait(new DeleteAssetCommand(id));
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(path = "/asset/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateAsset(@RequestBody AssetDTO asset, @PathVariable String id) {
        if (!id.equals(asset.id))
            return ResponseEntity.badRequest().body("{\"message\":\"Idnumber in body does not match resource id.\"}");

        log.info("Updating:" + asset.id);
        commandBus.sendAndWait(new UpdateAssetCommand(asset));
        return ResponseEntity.ok("{\"id:\":\"" + asset.id + "\"}");
    }

    @GetMapping("/assets")
    public List<AssetDTO> assets() {
        return assetRepository.findAll().stream()
                .map(AssetEntity::convertToDTO).collect(Collectors.toList());
    }

}
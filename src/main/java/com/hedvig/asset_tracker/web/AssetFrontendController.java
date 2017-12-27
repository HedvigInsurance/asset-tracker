package com.hedvig.asset_tracker.web;

import com.hedvig.asset_tracker.query.AssetRepository;
import com.hedvig.asset_tracker.web.dto.AssetDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/frontend")
public class AssetFrontendController {

    private final AssetRepository repository;

    @Autowired
    public AssetFrontendController(AssetRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/assets")
    public List<AssetDTO> assets() {
        return repository.findAll().stream().map(a -> a.convertToDTO()).collect(Collectors.toList());
    }

}

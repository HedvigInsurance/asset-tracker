package com.hedvig.asset_tracker.query;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface FileUploadRepository extends JpaRepository<UploadFile, Integer> {

    Optional<UploadFile> findByImageId(UUID id);
}
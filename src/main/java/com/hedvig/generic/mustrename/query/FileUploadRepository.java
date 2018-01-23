package com.hedvig.generic.mustrename.query;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FileUploadRepository extends JpaRepository<UploadFile, Integer> {

    Optional<UploadFile> findByImageId(UUID id);

}

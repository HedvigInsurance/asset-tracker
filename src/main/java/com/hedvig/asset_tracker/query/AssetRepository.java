package com.hedvig.asset_tracker.query;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssetRepository extends JpaRepository<AssetEntity, String> {
    Optional<AssetEntity> findById(String s);

    List<AssetEntity> findByUserId(String s);
}

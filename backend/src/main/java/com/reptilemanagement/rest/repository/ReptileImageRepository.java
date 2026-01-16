package com.reptilemanagement.rest.repository;

import com.reptilemanagement.domain.ReptileImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JPA repository for ReptileImage entities.
 * Provides CRUD operations and custom queries for reptile images.
 */
@Repository
public interface ReptileImageRepository extends JpaRepository<ReptileImage, Long> {
    /**
     * Finds all images for a specific reptile.
     * 
     * @param reptileId the reptile ID
     * @return list of images for the reptile
     */
    List<ReptileImage> findByReptileId(Long reptileId);

    /**
     * Finds all images for a specific reptile, ordered by upload date descending.
     * 
     * @param reptileId the reptile ID
     * @return list of images for the reptile, most recent first
     */
    List<ReptileImage> findByReptileIdOrderByUploadedAtDesc(Long reptileId);

    /**
     * Counts all images for a specific reptile.
     * 
     * @param reptileId the reptile ID
     * @return number of images
     */
    long countByReptileId(Long reptileId);

    /**
     * Deletes all images for a specific reptile.
     * 
     * @param reptileId the reptile ID
     */
    void deleteByReptileId(Long reptileId);
}

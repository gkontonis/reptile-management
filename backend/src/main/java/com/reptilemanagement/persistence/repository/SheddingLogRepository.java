package com.reptilemanagement.persistence.repository;

import com.reptilemanagement.persistence.domain.SheddingLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for SheddingLog entity operations.
 * Extends JpaRepository to provide basic CRUD operations and custom query methods.
 */
@Repository
public interface SheddingLogRepository extends JpaRepository<SheddingLog, Long> {

    /**
     * Finds all shedding logs for a specific reptile.
     * @param reptileId the ID of the reptile
     * @return list of shedding logs for the specified reptile, ordered by shedding date descending
     */
    List<SheddingLog> findByReptileIdOrderBySheddingDateDesc(Long reptileId);

    /**
     * Finds shedding logs for a specific reptile within a date range.
     * @param reptileId the ID of the reptile
     * @param startDate the start date of the range
     * @param endDate the end date of the range
     * @return list of shedding logs within the date range, ordered by shedding date descending
     */
    List<SheddingLog> findByReptileIdAndSheddingDateBetweenOrderBySheddingDateDesc(
            Long reptileId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Finds the most recent shedding log for a specific reptile.
     * @param reptileId the ID of the reptile
     * @return the most recent shedding log, or null if none exists
     */
    SheddingLog findTopByReptileIdOrderBySheddingDateDesc(Long reptileId);

    /**
     * Finds shedding logs by shed quality.
     * @param shedQuality the quality of the shed
     * @return list of shedding logs with the specified quality
     */
    List<SheddingLog> findByShedQuality(String shedQuality);

    /**
     * Counts shedding logs for a specific reptile.
     * @param reptileId the ID of the reptile
     * @return count of shedding logs for the specified reptile
     */
    long countByReptileId(Long reptileId);

    /**
     * Finds shedding logs where the reptile ate the shed skin.
     * @param reptileId the ID of the reptile
     * @return list of shedding logs where the reptile ate its shed
     */
    @Query("SELECT s FROM SheddingLog s WHERE s.reptileId = :reptileId AND s.ateShed = true ORDER BY s.sheddingDate DESC")
    List<SheddingLog> findAteShedByReptileId(@Param("reptileId") Long reptileId);
}
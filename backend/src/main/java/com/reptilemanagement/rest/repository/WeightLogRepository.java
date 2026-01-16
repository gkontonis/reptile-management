package com.reptilemanagement.rest.repository;

import com.reptilemanagement.domain.WeightLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for WeightLog entity operations.
 * Extends JpaRepository to provide basic CRUD operations and custom query methods.
 */
@Repository
public interface WeightLogRepository extends JpaRepository<WeightLog, Long> {

    /**
     * Finds all weight logs for a specific reptile.
     * @param reptileId the ID of the reptile
     * @return list of weight logs for the specified reptile, ordered by measurement date descending
     */
    List<WeightLog> findByReptileIdOrderByMeasurementDateDesc(Long reptileId);

    /**
     * Finds weight logs for a specific reptile within a date range.
     * @param reptileId the ID of the reptile
     * @param startDate the start date of the range
     * @param endDate the end date of the range
     * @return list of weight logs within the date range, ordered by measurement date descending
     */
    List<WeightLog> findByReptileIdAndMeasurementDateBetweenOrderByMeasurementDateDesc(
            Long reptileId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Finds the most recent weight log for a specific reptile.
     * @param reptileId the ID of the reptile
     * @return the most recent weight log, or null if none exists
     */
    WeightLog findTopByReptileIdOrderByMeasurementDateDesc(Long reptileId);

    /**
     * Finds the most recent weight measurement for a specific reptile.
     * @param reptileId the ID of the reptile
     * @return the weight in grams, or null if no measurements exist
     */
    @Query("SELECT w.weightGrams FROM WeightLog w WHERE w.reptileId = :reptileId ORDER BY w.measurementDate DESC")
    List<java.math.BigDecimal> findWeightHistoryByReptileId(@Param("reptileId") Long reptileId);

    /**
     * Counts weight logs for a specific reptile.
     * @param reptileId the ID of the reptile
     * @return count of weight logs for the specified reptile
     */
    long countByReptileId(Long reptileId);

    /**
     * Finds weight logs with weights above a certain threshold.
     * @param reptileId the ID of the reptile
     * @param minWeight the minimum weight threshold
     * @return list of weight logs above the threshold
     */
    @Query("SELECT w FROM WeightLog w WHERE w.reptileId = :reptileId AND w.weightGrams >= :minWeight ORDER BY w.measurementDate DESC")
    List<WeightLog> findWeightsAboveThreshold(@Param("reptileId") Long reptileId, @Param("minWeight") java.math.BigDecimal minWeight);
}
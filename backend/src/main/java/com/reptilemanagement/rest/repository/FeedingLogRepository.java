package com.reptilemanagement.rest.repository;

import com.reptilemanagement.domain.FeedingLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for FeedingLog entity operations.
 * Extends JpaRepository to provide basic CRUD operations and custom query methods.
 */
@Repository
public interface FeedingLogRepository extends JpaRepository<FeedingLog, Long> {

    /**
     * Finds all feeding logs for a specific reptile.
     * @param reptileId the ID of the reptile
     * @return list of feeding logs for the specified reptile, ordered by feeding date descending
     */
    List<FeedingLog> findByReptileIdOrderByFeedingDateDesc(Long reptileId);

    /**
     * Finds feeding logs for a specific reptile within a date range.
     * @param reptileId the ID of the reptile
     * @param startDate the start date of the range
     * @param endDate the end date of the range
     * @return list of feeding logs within the date range, ordered by feeding date descending
     */
    List<FeedingLog> findByReptileIdAndFeedingDateBetweenOrderByFeedingDateDesc(
            Long reptileId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Finds the most recent feeding log for a specific reptile.
     * @param reptileId the ID of the reptile
     * @return the most recent feeding log, or null if none exists
     */
    FeedingLog findTopByReptileIdOrderByFeedingDateDesc(Long reptileId);

    /**
     * Finds feeding logs by food type.
     * @param foodType the type of food
     * @return list of feeding logs with the specified food type
     */
    List<FeedingLog> findByFoodType(String foodType);

    /**
     * Counts feeding logs for a specific reptile.
     * @param reptileId the ID of the reptile
     * @return count of feeding logs for the specified reptile
     */
    long countByReptileId(Long reptileId);

    /**
     * Finds feeding logs where the reptile did not eat.
     * @param reptileId the ID of the reptile
     * @return list of feeding logs where the reptile refused food
     */
    @Query("SELECT f FROM FeedingLog f WHERE f.reptileId = :reptileId AND f.ate = false ORDER BY f.feedingDate DESC")
    List<FeedingLog> findMissedFeedingsByReptileId(@Param("reptileId") Long reptileId);
}
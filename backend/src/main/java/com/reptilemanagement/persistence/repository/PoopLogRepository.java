package com.reptilemanagement.persistence.repository;

import com.reptilemanagement.persistence.domain.PoopLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for PoopLog entity operations.
 * Extends JpaRepository to provide basic CRUD operations and custom query methods.
 */
@Repository
public interface PoopLogRepository extends JpaRepository<PoopLog, Long> {

    /**
     * Finds all poop logs for a specific reptile.
     * @param reptileId the ID of the reptile
     * @return list of poop logs for the specified reptile, ordered by poop date descending
     */
    List<PoopLog> findByReptileIdOrderByPoopDateDesc(Long reptileId);

    /**
     * Finds poop logs for a specific reptile within a date range.
     * @param reptileId the ID of the reptile
     * @param startDate the start date of the range
     * @param endDate the end date of the range
     * @return list of poop logs within the date range, ordered by poop date descending
     */
    List<PoopLog> findByReptileIdAndPoopDateBetweenOrderByPoopDateDesc(
            Long reptileId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Finds the most recent poop log for a specific reptile.
     * @param reptileId the ID of the reptile
     * @return the most recent poop log, or null if none exists
     */
    PoopLog findTopByReptileIdOrderByPoopDateDesc(Long reptileId);

    /**
     * Counts poop logs for a specific reptile.
     * @param reptileId the ID of the reptile
     * @return count of poop logs for the specified reptile
     */
    long countByReptileId(Long reptileId);

    /**
     * Finds poop logs where parasites were observed.
     * @param reptileId the ID of the reptile
     * @return list of poop logs with parasites present
     */
    @Query("SELECT p FROM PoopLog p WHERE p.reptileId = :reptileId AND p.parasitesPresent = true ORDER BY p.poopDate DESC")
    List<PoopLog> findWithParasitesByReptileId(@Param("reptileId") Long reptileId);
}

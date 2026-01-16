package com.reptilemanagement.persistence.repository;

import com.reptilemanagement.persistence.domain.EnclosureCleaning;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for EnclosureCleaning entity operations.
 * Extends JpaRepository to provide basic CRUD operations and custom query methods.
 */
@Repository
public interface EnclosureCleaningRepository extends JpaRepository<EnclosureCleaning, Long> {

    /**
     * Finds all enclosure cleaning logs for a specific enclosure.
     * @param enclosureId the ID of the enclosure
     * @return list of cleaning logs for the specified enclosure, ordered by cleaning date descending
     */
    List<EnclosureCleaning> findByEnclosureIdOrderByCleaningDateDesc(Long enclosureId);

    /**
     * Finds enclosure cleaning logs for a specific enclosure within a date range.
     * @param enclosureId the ID of the enclosure
     * @param startDate the start date of the range
     * @param endDate the end date of the range
     * @return list of cleaning logs within the date range, ordered by cleaning date descending
     */
    List<EnclosureCleaning> findByEnclosureIdAndCleaningDateBetweenOrderByCleaningDateDesc(
            Long enclosureId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Finds the most recent enclosure cleaning log for a specific enclosure.
     * @param enclosureId the ID of the enclosure
     * @return the most recent cleaning log, or null if none exists
     */
    EnclosureCleaning findTopByEnclosureIdOrderByCleaningDateDesc(Long enclosureId);

    /**
     * Finds enclosure cleaning logs by cleaning type.
     * @param cleaningType the type of cleaning
     * @return list of cleaning logs with the specified type
     */
    List<EnclosureCleaning> findByCleaningType(EnclosureCleaning.CleaningType cleaningType);

    /**
     * Counts enclosure cleaning logs for a specific enclosure.
     * @param enclosureId the ID of the enclosure
     * @return count of cleaning logs for the specified enclosure
     */
    long countByEnclosureId(Long enclosureId);

    /**
     * Finds enclosure cleaning logs where disinfection was performed.
     * @param enclosureId the ID of the enclosure
     * @return list of cleaning logs where disinfection occurred
     */
    @Query("SELECT c FROM EnclosureCleaning c WHERE c.enclosureId = :enclosureId AND c.disinfected = true ORDER BY c.cleaningDate DESC")
    List<EnclosureCleaning> findDisinfectionsByEnclosureId(@Param("enclosureId") Long enclosureId);

    /**
     * Finds enclosure cleaning logs where substrate was changed.
     * @param enclosureId the ID of the enclosure
     * @return list of cleaning logs where substrate was changed
     */
    @Query("SELECT c FROM EnclosureCleaning c WHERE c.enclosureId = :enclosureId AND c.substrateChanged = true ORDER BY c.cleaningDate DESC")
    List<EnclosureCleaning> findSubstrateChangesByEnclosureId(@Param("enclosureId") Long enclosureId);
}
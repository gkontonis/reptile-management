package com.reptilemanagement.persistence.repository;

import com.reptilemanagement.persistence.domain.Enclosure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Enclosure entity operations.
 * Extends JpaRepository to provide basic CRUD operations and custom query methods.
 */
@Repository
public interface EnclosureRepository extends JpaRepository<Enclosure, Long> {

    // ==================== User-scoped queries ====================

    /**
     * Finds all enclosures belonging to a specific user.
     * @param userId the ID of the user
     * @return list of enclosures owned by the user
     */
    List<Enclosure> findByUserId(Long userId);

    /**
     * Finds an enclosure by ID only if it belongs to the given user.
     * @param id the enclosure ID
     * @param userId the user ID
     * @return the enclosure if found and owned by the user
     */
    Optional<Enclosure> findByIdAndUserId(Long id, Long userId);

    /**
     * Checks if an enclosure exists and belongs to the given user.
     * @param id the enclosure ID
     * @param userId the user ID
     * @return true if the enclosure exists and belongs to the user
     */
    boolean existsByIdAndUserId(Long id, Long userId);

    /**
     * Finds enclosures by type for a specific user.
     * @param userId the user ID
     * @param type the enclosure type
     * @return list of enclosures matching the type for the user
     */
    List<Enclosure> findByUserIdAndType(Long userId, Enclosure.EnclosureType type);

    /**
     * Counts enclosures belonging to a specific user.
     * @param userId the user ID
     * @return count of enclosures owned by the user
     */
    long countByUserId(Long userId);

    /**
     * Counts enclosures by type for a specific user.
     * @param userId the user ID
     * @param type the enclosure type
     * @return count of enclosures matching the type for the user
     */
    long countByUserIdAndType(Long userId, Enclosure.EnclosureType type);

    /**
     * Finds enclosures by name for a specific user (case-insensitive).
     * @param userId the user ID
     * @param name the name search term
     * @return list of enclosures matching the name for the user
     */
    @Query("SELECT e FROM Enclosure e WHERE e.userId = :userId AND LOWER(e.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Enclosure> findByUserIdAndNameContainingIgnoreCase(@Param("userId") Long userId, @Param("name") String name);

    /**
     * Finds occupied enclosure IDs for a specific user's reptiles.
     * @param userId the user ID
     * @return list of enclosure IDs that have the user's reptiles assigned
     */
    @Query("SELECT DISTINCT r.enclosureId FROM Reptile r WHERE r.enclosureId IS NOT NULL AND r.userId = :userId")
    List<Long> findOccupiedEnclosureIdsByUserId(@Param("userId") Long userId);

    /**
     * Finds empty enclosures for a specific user.
     * @param userId the user ID
     * @return list of the user's enclosures that have no reptiles assigned
     */
    @Query("SELECT e FROM Enclosure e WHERE e.userId = :userId AND e.id NOT IN (SELECT r.enclosureId FROM Reptile r WHERE r.enclosureId IS NOT NULL AND r.userId = :userId)")
    List<Enclosure> findEmptyEnclosuresByUserId(@Param("userId") Long userId);

    // ==================== Non-scoped queries (legacy) ====================

    /**
     * Finds enclosures by type.
     * @param type the type of enclosure
     * @return list of enclosures of the specified type
     */
    List<Enclosure> findByType(Enclosure.EnclosureType type);

    /**
     * Finds enclosures by name containing the search term (case-insensitive).
     * @param name the name search term
     * @return list of enclosures whose name contains the search term
     */
    @Query("SELECT e FROM Enclosure e WHERE LOWER(e.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Enclosure> findByNameContainingIgnoreCase(@Param("name") String name);

    /**
     * Finds enclosures that are currently occupied by reptiles.
     * @return list of enclosure IDs that have reptiles assigned
     */
    @Query("SELECT DISTINCT r.enclosureId FROM Reptile r WHERE r.enclosureId IS NOT NULL")
    List<Long> findOccupiedEnclosureIds();

    /**
     * Finds enclosures that are currently empty (no reptiles assigned).
     * @return list of empty enclosures
     */
    @Query("SELECT e FROM Enclosure e WHERE e.id NOT IN (SELECT r.enclosureId FROM Reptile r WHERE r.enclosureId IS NOT NULL)")
    List<Enclosure> findEmptyEnclosures();

    /**
     * Counts enclosures by type.
     * @param type the type of enclosure
     * @return count of enclosures of the specified type
     */
    long countByType(Enclosure.EnclosureType type);
}
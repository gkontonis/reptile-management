package com.reptilemanagement.persistence.repository;

import com.reptilemanagement.persistence.domain.Reptile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Reptile entity operations.
 * Extends JpaRepository to provide basic CRUD operations and custom query methods.
 */
@Repository
public interface ReptileRepository extends JpaRepository<Reptile, Long> {

    // ==================== User-scoped queries ====================

    /**
     * Finds all reptiles belonging to a specific user.
     * @param userId the ID of the user
     * @return list of reptiles owned by the user
     */
    List<Reptile> findByUserId(Long userId);

    /**
     * Finds a reptile by ID only if it belongs to the given user.
     * @param id the reptile ID
     * @param userId the user ID
     * @return the reptile if found and owned by the user
     */
    Optional<Reptile> findByIdAndUserId(Long id, Long userId);

    /**
     * Checks if a reptile exists and belongs to the given user.
     * @param id the reptile ID
     * @param userId the user ID
     * @return true if the reptile exists and belongs to the user
     */
    boolean existsByIdAndUserId(Long id, Long userId);

    /**
     * Finds reptiles by status for a specific user.
     * @param userId the user ID
     * @param status the reptile status
     * @return list of reptiles matching the status for the user
     */
    List<Reptile> findByUserIdAndStatus(Long userId, Reptile.ReptileStatus status);

    /**
     * Counts reptiles belonging to a specific user.
     * @param userId the user ID
     * @return count of reptiles owned by the user
     */
    long countByUserId(Long userId);

    /**
     * Counts reptiles by status for a specific user.
     * @param userId the user ID
     * @param status the reptile status
     * @return count of reptiles matching the status for the user
     */
    long countByUserIdAndStatus(Long userId, Reptile.ReptileStatus status);

    /**
     * Finds reptiles by species for a specific user (case-insensitive).
     * @param userId the user ID
     * @param species the species search term
     * @return list of reptiles matching the species for the user
     */
    @Query("SELECT r FROM Reptile r WHERE r.userId = :userId AND LOWER(r.species) LIKE LOWER(CONCAT('%', :species, '%'))")
    List<Reptile> findByUserIdAndSpeciesContainingIgnoreCase(@Param("userId") Long userId, @Param("species") String species);

    /**
     * Finds reptiles by name for a specific user (case-insensitive).
     * @param userId the user ID
     * @param name the name search term
     * @return list of reptiles matching the name for the user
     */
    @Query("SELECT r FROM Reptile r WHERE r.userId = :userId AND LOWER(r.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Reptile> findByUserIdAndNameContainingIgnoreCase(@Param("userId") Long userId, @Param("name") String name);

    /**
     * Finds reptiles in a specific enclosure for a specific user.
     * @param userId the user ID
     * @param enclosureId the enclosure ID
     * @return list of reptiles in the enclosure owned by the user
     */
    List<Reptile> findByUserIdAndEnclosureId(Long userId, Long enclosureId);

    // ==================== Non-scoped queries (legacy) ====================

    /**
     * Finds all reptiles that are currently active.
     * @return list of active reptiles
     */
    List<Reptile> findByStatus(Reptile.ReptileStatus status);

    /**
     * Finds all reptiles in a specific enclosure.
     * @param enclosureId the ID of the enclosure
     * @return list of reptiles in the specified enclosure
     */
    List<Reptile> findByEnclosureId(Long enclosureId);

    /**
     * Finds reptiles by species.
     * @param species the species to search for
     * @return list of reptiles of the specified species
     */
    List<Reptile> findBySpecies(String species);

    /**
     * Finds reptiles by species containing the search term (case-insensitive).
     * @param species the species search term
     * @return list of reptiles whose species contains the search term
     */
    @Query("SELECT r FROM Reptile r WHERE LOWER(r.species) LIKE LOWER(CONCAT('%', :species, '%'))")
    List<Reptile> findBySpeciesContainingIgnoreCase(@Param("species") String species);

    /**
     * Finds reptiles by name containing the search term (case-insensitive).
     * @param name the name search term
     * @return list of reptiles whose name contains the search term
     */
    @Query("SELECT r FROM Reptile r WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Reptile> findByNameContainingIgnoreCase(@Param("name") String name);

    /**
     * Counts reptiles by status.
     * @param status the status to count
     * @return count of reptiles with the specified status
     */
    long countByStatus(Reptile.ReptileStatus status);
}
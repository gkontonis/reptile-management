package com.reptilemanagement.rest.repository;

import com.reptilemanagement.domain.Reptile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Reptile entity operations.
 * Extends JpaRepository to provide basic CRUD operations and custom query methods.
 */
@Repository
public interface ReptileRepository extends JpaRepository<Reptile, Long> {

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
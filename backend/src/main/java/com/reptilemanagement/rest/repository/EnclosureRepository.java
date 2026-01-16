package com.reptilemanagement.rest.repository;

import com.reptilemanagement.domain.Enclosure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Enclosure entity operations.
 * Extends JpaRepository to provide basic CRUD operations and custom query methods.
 */
@Repository
public interface EnclosureRepository extends JpaRepository<Enclosure, Long> {

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
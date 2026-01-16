package com.reptilemanagement.rest.controller;

import com.reptilemanagement.persistence.domain.Enclosure;
import com.reptilemanagement.persistence.dto.EnclosureDto;
import com.reptilemanagement.rest.service.EnclosureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * REST controller for managing enclosure operations.
 * Provides endpoints for CRUD operations on enclosures.
 */
@RestController
@RequestMapping("/api/enclosures")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('USER')")
public class EnclosureController {

    private final EnclosureService enclosureService;

    /**
     * Creates a new enclosure.
     * @param enclosureDto the enclosure data
     * @return the created enclosure
     */
    @PostMapping
    public ResponseEntity<EnclosureDto> createEnclosure(@Valid @RequestBody EnclosureDto enclosureDto) {
        log.info("REST request to create enclosure: {}", enclosureDto.getName());

        EnclosureDto result = enclosureService.createEnclosure(enclosureDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    /**
     * Retrieves all enclosures.
     * @return list of all enclosures
     */
    @GetMapping
    public ResponseEntity<List<EnclosureDto>> getAllEnclosures() {
        log.debug("REST request to get all enclosures");

        List<EnclosureDto> enclosures = enclosureService.getAllEnclosures();
        return ResponseEntity.ok(enclosures);
    }

    /**
     * Retrieves an enclosure by ID.
     * @param id the enclosure ID
     * @return the enclosure
     */
    @GetMapping("/{id}")
    public ResponseEntity<EnclosureDto> getEnclosure(@PathVariable Long id) {
        log.debug("REST request to get enclosure: {}", id);

        return enclosureService.getEnclosureById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Retrieves enclosures by type.
     * @param type the enclosure type
     * @return list of enclosures of the specified type
     */
    @GetMapping("/type/{type}")
    public ResponseEntity<List<EnclosureDto>> getEnclosuresByType(@PathVariable Enclosure.EnclosureType type) {
        log.debug("REST request to get enclosures by type: {}", type);

        List<EnclosureDto> enclosures = enclosureService.getEnclosuresByType(type);
        return ResponseEntity.ok(enclosures);
    }

    /**
     * Searches enclosures by name.
     * @param name the name to search for
     * @return list of enclosures matching the name
     */
    @GetMapping("/search/name")
    public ResponseEntity<List<EnclosureDto>> searchByName(@RequestParam String name) {
        log.debug("REST request to search enclosures by name: {}", name);

        List<EnclosureDto> enclosures = enclosureService.getEnclosuresByName(name);
        return ResponseEntity.ok(enclosures);
    }

    /**
     * Retrieves enclosures that are currently occupied by reptiles.
     * @return list of occupied enclosure IDs
     */
    @GetMapping("/occupied")
    public ResponseEntity<List<Long>> getOccupiedEnclosureIds() {
        log.debug("REST request to get occupied enclosure IDs");

        List<Long> occupiedIds = enclosureService.getOccupiedEnclosureIds();
        return ResponseEntity.ok(occupiedIds);
    }

    /**
     * Retrieves enclosures that are currently empty.
     * @return list of empty enclosures
     */
    @GetMapping("/empty")
    public ResponseEntity<List<EnclosureDto>> getEmptyEnclosures() {
        log.debug("REST request to get empty enclosures");

        List<EnclosureDto> enclosures = enclosureService.getEmptyEnclosures();
        return ResponseEntity.ok(enclosures);
    }

    /**
     * Checks if an enclosure can be safely deleted.
     * @param id the enclosure ID
     * @return true if the enclosure can be deleted
     */
    @GetMapping("/{id}/can-delete")
    public ResponseEntity<Boolean> canDeleteEnclosure(@PathVariable Long id) {
        log.debug("REST request to check if enclosure can be deleted: {}", id);

        boolean canDelete = enclosureService.canDeleteEnclosure(id);
        return ResponseEntity.ok(canDelete);
    }

    /**
     * Updates an existing enclosure.
     * @param id the enclosure ID
     * @param enclosureDto the updated enclosure data
     * @return the updated enclosure
     */
    @PutMapping("/{id}")
    public ResponseEntity<EnclosureDto> updateEnclosure(@PathVariable Long id, @Valid @RequestBody EnclosureDto enclosureDto) {
        log.info("REST request to update enclosure: {}", id);

        return enclosureService.updateEnclosure(id, enclosureDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Deletes an enclosure.
     * @param id the enclosure ID
     * @return success status
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEnclosure(@PathVariable Long id) {
        log.info("REST request to delete enclosure: {}", id);

        boolean success = enclosureService.deleteEnclosure(id);
        return success ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    /**
     * Gets enclosure statistics.
     * @return statistics data
     */
    @GetMapping("/statistics")
    public ResponseEntity<EnclosureService.EnclosureStatistics> getStatistics() {
        log.debug("REST request to get enclosure statistics");

        EnclosureService.EnclosureStatistics statistics = enclosureService.getStatistics();
        return ResponseEntity.ok(statistics);
    }
}
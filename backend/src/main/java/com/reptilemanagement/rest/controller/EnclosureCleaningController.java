package com.reptilemanagement.rest.controller;

import com.reptilemanagement.domain.EnclosureCleaning;
import com.reptilemanagement.dto.EnclosureCleaningDto;
import com.reptilemanagement.rest.service.EnclosureCleaningService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

/**
 * REST controller for managing enclosure cleaning operations.
 * Provides endpoints for CRUD operations on enclosure cleaning logs.
 */
@RestController
@RequestMapping("/api/enclosure-cleanings")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('USER')")
public class EnclosureCleaningController {

    private final EnclosureCleaningService enclosureCleaningService;

    /**
     * Creates a new enclosure cleaning log.
     * @param cleaningDto the cleaning log data
     * @return the created cleaning log
     */
    @PostMapping
    public ResponseEntity<EnclosureCleaningDto> createCleaningLog(@Valid @RequestBody EnclosureCleaningDto cleaningDto) {
        log.info("REST request to create enclosure cleaning log for enclosure: {}", cleaningDto.getEnclosureId());

        EnclosureCleaningDto result = enclosureCleaningService.createEnclosureCleaning(cleaningDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    /**
     * Retrieves an enclosure cleaning log by ID.
     * @param id the cleaning log ID
     * @return the cleaning log
     */
    @GetMapping("/{id}")
    public ResponseEntity<EnclosureCleaningDto> getCleaningLog(@PathVariable Long id) {
        log.debug("REST request to get enclosure cleaning log: {}", id);

        return enclosureCleaningService.getEnclosureCleaningById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Retrieves cleaning logs for a specific enclosure.
     * @param enclosureId the enclosure ID
     * @return list of cleaning logs for the enclosure
     */
    @GetMapping("/enclosure/{enclosureId}")
    public ResponseEntity<List<EnclosureCleaningDto>> getCleaningLogsByEnclosure(@PathVariable Long enclosureId) {
        log.debug("REST request to get cleaning logs for enclosure: {}", enclosureId);

        List<EnclosureCleaningDto> cleanings = enclosureCleaningService.getEnclosureCleaningsByEnclosureId(enclosureId);
        return ResponseEntity.ok(cleanings);
    }

    /**
     * Retrieves cleaning logs for an enclosure within a date range.
     * @param enclosureId the enclosure ID
     * @param startDate the start date
     * @param endDate the end date
     * @return list of cleaning logs within the date range
     */
    @GetMapping("/enclosure/{enclosureId}/date-range")
    public ResponseEntity<List<EnclosureCleaningDto>> getCleaningLogsByDateRange(
            @PathVariable Long enclosureId,
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate) {
        log.debug("REST request to get cleaning logs for enclosure {} between {} and {}", enclosureId, startDate, endDate);

        List<EnclosureCleaningDto> cleanings = enclosureCleaningService.getEnclosureCleaningsByEnclosureIdAndDateRange(enclosureId, startDate, endDate);
        return ResponseEntity.ok(cleanings);
    }

    /**
     * Retrieves the most recent cleaning log for an enclosure.
     * @param enclosureId the enclosure ID
     * @return the most recent cleaning log
     */
    @GetMapping("/enclosure/{enclosureId}/latest")
    public ResponseEntity<EnclosureCleaningDto> getLatestCleaningLog(@PathVariable Long enclosureId) {
        log.debug("REST request to get latest cleaning log for enclosure: {}", enclosureId);

        return enclosureCleaningService.getLatestEnclosureCleaning(enclosureId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Retrieves disinfection logs for an enclosure.
     * @param enclosureId the enclosure ID
     * @return list of disinfection logs
     */
    @GetMapping("/enclosure/{enclosureId}/disinfections")
    public ResponseEntity<List<EnclosureCleaningDto>> getDisinfections(@PathVariable Long enclosureId) {
        log.debug("REST request to get disinfection logs for enclosure: {}", enclosureId);

        List<EnclosureCleaningDto> disinfections = enclosureCleaningService.getDisinfections(enclosureId);
        return ResponseEntity.ok(disinfections);
    }

    /**
     * Retrieves substrate change logs for an enclosure.
     * @param enclosureId the enclosure ID
     * @return list of substrate change logs
     */
    @GetMapping("/enclosure/{enclosureId}/substrate-changes")
    public ResponseEntity<List<EnclosureCleaningDto>> getSubstrateChanges(@PathVariable Long enclosureId) {
        log.debug("REST request to get substrate change logs for enclosure: {}", enclosureId);

        List<EnclosureCleaningDto> substrateChanges = enclosureCleaningService.getSubstrateChanges(enclosureId);
        return ResponseEntity.ok(substrateChanges);
    }

    /**
     * Updates an existing enclosure cleaning log.
     * @param id the cleaning log ID
     * @param cleaningDto the updated cleaning log data
     * @return the updated cleaning log
     */
    @PutMapping("/{id}")
    public ResponseEntity<EnclosureCleaningDto> updateCleaningLog(@PathVariable Long id, @Valid @RequestBody EnclosureCleaningDto cleaningDto) {
        log.info("REST request to update enclosure cleaning log: {}", id);

        return enclosureCleaningService.updateEnclosureCleaning(id, cleaningDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Deletes an enclosure cleaning log.
     * @param id the cleaning log ID
     * @return success status
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCleaningLog(@PathVariable Long id) {
        log.info("REST request to delete enclosure cleaning log: {}", id);

        boolean success = enclosureCleaningService.deleteEnclosureCleaning(id);
        return success ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    /**
     * Gets enclosure cleaning statistics for an enclosure.
     * @param enclosureId the enclosure ID
     * @return statistics data
     */
    @GetMapping("/enclosure/{enclosureId}/statistics")
    public ResponseEntity<EnclosureCleaningService.EnclosureCleaningStatistics> getStatistics(@PathVariable Long enclosureId) {
        log.debug("REST request to get enclosure cleaning statistics for enclosure: {}", enclosureId);

        EnclosureCleaningService.EnclosureCleaningStatistics statistics = enclosureCleaningService.getCleaningStatistics(enclosureId);
        return ResponseEntity.ok(statistics);
    }
}
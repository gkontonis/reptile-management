package com.reptilemanagement.rest.controller;

import com.reptilemanagement.persistence.dto.PoopLogDto;
import com.reptilemanagement.rest.service.PoopLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

/**
 * REST controller for managing poop log operations.
 * Provides endpoints for CRUD operations on poop logs.
 */
@RestController
@RequestMapping("/api/poop-logs")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('USER')")
public class PoopLogController {

    private final PoopLogService poopLogService;

    /**
     * Creates a new poop log entry.
     * @param poopLogDto the poop log data
     * @return the created poop log
     */
    @PostMapping
    public ResponseEntity<PoopLogDto> createPoopLog(@Valid @RequestBody PoopLogDto poopLogDto) {
        log.info("REST request to create poop log for reptile: {}", poopLogDto.getReptileId());

        PoopLogDto result = poopLogService.createPoopLog(poopLogDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    /**
     * Retrieves a poop log by ID.
     * @param id the poop log ID
     * @return the poop log
     */
    @GetMapping("/{id}")
    public ResponseEntity<PoopLogDto> getPoopLog(@PathVariable Long id) {
        log.debug("REST request to get poop log: {}", id);

        return poopLogService.getPoopLogById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Retrieves all poop logs for a specific reptile.
     * @param reptileId the reptile ID
     * @return list of poop logs for the reptile
     */
    @GetMapping("/reptile/{reptileId}")
    public ResponseEntity<List<PoopLogDto>> getPoopLogsByReptile(@PathVariable Long reptileId) {
        log.debug("REST request to get poop logs for reptile: {}", reptileId);

        List<PoopLogDto> poopLogs = poopLogService.getPoopLogsByReptileId(reptileId);
        return ResponseEntity.ok(poopLogs);
    }

    /**
     * Retrieves poop logs for a reptile within a date range.
     * @param reptileId the reptile ID
     * @param startDate the start date
     * @param endDate the end date
     * @return list of poop logs within the date range
     */
    @GetMapping("/reptile/{reptileId}/range")
    public ResponseEntity<List<PoopLogDto>> getPoopLogsByDateRange(
            @PathVariable Long reptileId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        log.debug("REST request to get poop logs for reptile {} between {} and {}", reptileId, startDate, endDate);

        List<PoopLogDto> poopLogs = poopLogService.getPoopLogsByReptileIdAndDateRange(reptileId, startDate, endDate);
        return ResponseEntity.ok(poopLogs);
    }

    /**
     * Retrieves the most recent poop log for a reptile.
     * @param reptileId the reptile ID
     * @return the latest poop log
     */
    @GetMapping("/reptile/{reptileId}/latest")
    public ResponseEntity<PoopLogDto> getLatestPoopLog(@PathVariable Long reptileId) {
        log.debug("REST request to get latest poop log for reptile: {}", reptileId);

        return poopLogService.getLatestPoopLog(reptileId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Retrieves poop logs where parasites were observed.
     * @param reptileId the reptile ID
     * @return list of poop logs with parasites
     */
    @GetMapping("/reptile/{reptileId}/parasites")
    public ResponseEntity<List<PoopLogDto>> getPoopLogsWithParasites(@PathVariable Long reptileId) {
        log.debug("REST request to get poop logs with parasites for reptile: {}", reptileId);

        List<PoopLogDto> poopLogs = poopLogService.getPoopLogsWithParasites(reptileId);
        return ResponseEntity.ok(poopLogs);
    }

    /**
     * Updates an existing poop log.
     * @param id the poop log ID
     * @param poopLogDto the updated poop log data
     * @return the updated poop log
     */
    @PutMapping("/{id}")
    public ResponseEntity<PoopLogDto> updatePoopLog(@PathVariable Long id, @Valid @RequestBody PoopLogDto poopLogDto) {
        log.info("REST request to update poop log: {}", id);

        return poopLogService.updatePoopLog(id, poopLogDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Deletes a poop log.
     * @param id the poop log ID
     * @return success status
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePoopLog(@PathVariable Long id) {
        log.info("REST request to delete poop log: {}", id);

        boolean success = poopLogService.deletePoopLog(id);
        return success ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    /**
     * Gets poop statistics for a reptile.
     * @param reptileId the reptile ID
     * @return poop statistics
     */
    @GetMapping("/reptile/{reptileId}/statistics")
    public ResponseEntity<PoopLogService.PoopStatistics> getPoopStatistics(@PathVariable Long reptileId) {
        log.debug("REST request to get poop statistics for reptile: {}", reptileId);

        PoopLogService.PoopStatistics statistics = poopLogService.getPoopStatistics(reptileId);
        return ResponseEntity.ok(statistics);
    }
}

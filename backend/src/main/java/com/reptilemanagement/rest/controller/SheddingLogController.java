package com.reptilemanagement.rest.controller;

import com.reptilemanagement.persistence.dto.SheddingLogDto;
import com.reptilemanagement.rest.service.SheddingLogService;
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
 * REST controller for managing shedding log operations.
 * Provides endpoints for CRUD operations on shedding logs.
 */
@RestController
@RequestMapping("/api/shedding-logs")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('USER')")
public class SheddingLogController {

    private final SheddingLogService sheddingLogService;

    /**
     * Creates a new shedding log entry.
     * @param sheddingLogDto the shedding log data
     * @return the created shedding log
     */
    @PostMapping
    public ResponseEntity<SheddingLogDto> createSheddingLog(@Valid @RequestBody SheddingLogDto sheddingLogDto) {
        log.info("REST request to create shedding log for reptile: {}", sheddingLogDto.getReptileId());

        SheddingLogDto result = sheddingLogService.createSheddingLog(sheddingLogDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    /**
     * Retrieves a shedding log by ID.
     * @param id the shedding log ID
     * @return the shedding log
     */
    @GetMapping("/{id}")
    public ResponseEntity<SheddingLogDto> getSheddingLog(@PathVariable Long id) {
        log.debug("REST request to get shedding log: {}", id);

        return sheddingLogService.getSheddingLogById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Retrieves all shedding logs for a specific reptile.
     * @param reptileId the reptile ID
     * @return list of shedding logs for the reptile
     */
    @GetMapping("/reptile/{reptileId}")
    public ResponseEntity<List<SheddingLogDto>> getSheddingLogsByReptile(@PathVariable Long reptileId) {
        log.debug("REST request to get shedding logs for reptile: {}", reptileId);

        List<SheddingLogDto> sheddingLogs = sheddingLogService.getSheddingLogsByReptileId(reptileId);
        return ResponseEntity.ok(sheddingLogs);
    }

    /**
     * Retrieves shedding logs for a reptile within a date range.
     * @param reptileId the reptile ID
     * @param startDate the start date
     * @param endDate the end date
     * @return list of shedding logs within the date range
     */
    @GetMapping("/reptile/{reptileId}/range")
    public ResponseEntity<List<SheddingLogDto>> getSheddingLogsByDateRange(
            @PathVariable Long reptileId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        log.debug("REST request to get shedding logs for reptile {} between {} and {}", reptileId, startDate, endDate);

        List<SheddingLogDto> sheddingLogs = sheddingLogService.getSheddingLogsByReptileIdAndDateRange(reptileId, startDate, endDate);
        return ResponseEntity.ok(sheddingLogs);
    }

    /**
     * Retrieves the most recent shedding log for a reptile.
     * @param reptileId the reptile ID
     * @return the latest shedding log
     */
    @GetMapping("/reptile/{reptileId}/latest")
    public ResponseEntity<SheddingLogDto> getLatestSheddingLog(@PathVariable Long reptileId) {
        log.debug("REST request to get latest shedding log for reptile: {}", reptileId);

        return sheddingLogService.getLatestSheddingLog(reptileId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Retrieves shedding logs where the reptile ate the shed skin.
     * @param reptileId the reptile ID
     * @return list of shedding logs where the reptile ate its shed
     */
    @GetMapping("/reptile/{reptileId}/ate-shed")
    public ResponseEntity<List<SheddingLogDto>> getAteShedLogs(@PathVariable Long reptileId) {
        log.debug("REST request to get ate shed logs for reptile: {}", reptileId);

        List<SheddingLogDto> sheddingLogs = sheddingLogService.getAteShedLogs(reptileId);
        return ResponseEntity.ok(sheddingLogs);
    }

    /**
     * Updates an existing shedding log.
     * @param id the shedding log ID
     * @param sheddingLogDto the updated shedding log data
     * @return the updated shedding log
     */
    @PutMapping("/{id}")
    public ResponseEntity<SheddingLogDto> updateSheddingLog(@PathVariable Long id, @Valid @RequestBody SheddingLogDto sheddingLogDto) {
        log.info("REST request to update shedding log: {}", id);

        return sheddingLogService.updateSheddingLog(id, sheddingLogDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Deletes a shedding log.
     * @param id the shedding log ID
     * @return success status
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSheddingLog(@PathVariable Long id) {
        log.info("REST request to delete shedding log: {}", id);

        boolean success = sheddingLogService.deleteSheddingLog(id);
        return success ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    /**
     * Gets shedding statistics for a reptile.
     * @param reptileId the reptile ID
     * @return shedding statistics
     */
    @GetMapping("/reptile/{reptileId}/statistics")
    public ResponseEntity<SheddingLogService.SheddingStatistics> getSheddingStatistics(@PathVariable Long reptileId) {
        log.debug("REST request to get shedding statistics for reptile: {}", reptileId);

        SheddingLogService.SheddingStatistics statistics = sheddingLogService.getSheddingStatistics(reptileId);
        return ResponseEntity.ok(statistics);
    }
}
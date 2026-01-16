package com.reptilemanagement.rest.controller;

import com.reptilemanagement.dto.WeightLogDto;
import com.reptilemanagement.rest.service.WeightLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * REST controller for managing weight log operations.
 * Provides endpoints for CRUD operations on weight logs.
 */
@RestController
@RequestMapping("/api/weight-logs")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('USER')")
public class WeightLogController {

    private final WeightLogService weightLogService;

    /**
     * Creates a new weight log entry.
     * @param weightLogDto the weight log data
     * @return the created weight log
     */
    @PostMapping
    public ResponseEntity<WeightLogDto> createWeightLog(@Valid @RequestBody WeightLogDto weightLogDto) {
        log.info("REST request to create weight log for reptile: {}", weightLogDto.getReptileId());

        WeightLogDto result = weightLogService.createWeightLog(weightLogDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    /**
     * Retrieves a weight log by ID.
     * @param id the weight log ID
     * @return the weight log
     */
    @GetMapping("/{id}")
    public ResponseEntity<WeightLogDto> getWeightLog(@PathVariable Long id) {
        log.debug("REST request to get weight log: {}", id);

        return weightLogService.getWeightLogById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Retrieves all weight logs for a specific reptile.
     * @param reptileId the reptile ID
     * @return list of weight logs for the reptile
     */
    @GetMapping("/reptile/{reptileId}")
    public ResponseEntity<List<WeightLogDto>> getWeightLogsByReptile(@PathVariable Long reptileId) {
        log.debug("REST request to get weight logs for reptile: {}", reptileId);

        List<WeightLogDto> weightLogs = weightLogService.getWeightLogsByReptileId(reptileId);
        return ResponseEntity.ok(weightLogs);
    }

    /**
     * Retrieves weight logs for a reptile within a date range.
     * @param reptileId the reptile ID
     * @param startDate the start date
     * @param endDate the end date
     * @return list of weight logs within the date range
     */
    @GetMapping("/reptile/{reptileId}/range")
    public ResponseEntity<List<WeightLogDto>> getWeightLogsByDateRange(
            @PathVariable Long reptileId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        log.debug("REST request to get weight logs for reptile {} between {} and {}", reptileId, startDate, endDate);

        List<WeightLogDto> weightLogs = weightLogService.getWeightLogsByReptileIdAndDateRange(reptileId, startDate, endDate);
        return ResponseEntity.ok(weightLogs);
    }

    /**
     * Retrieves the most recent weight log for a reptile.
     * @param reptileId the reptile ID
     * @return the latest weight log
     */
    @GetMapping("/reptile/{reptileId}/latest")
    public ResponseEntity<WeightLogDto> getLatestWeightLog(@PathVariable Long reptileId) {
        log.debug("REST request to get latest weight log for reptile: {}", reptileId);

        return weightLogService.getLatestWeightLog(reptileId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Retrieves the current weight for a reptile.
     * @param reptileId the reptile ID
     * @return the current weight in grams
     */
    @GetMapping("/reptile/{reptileId}/current-weight")
    public ResponseEntity<BigDecimal> getCurrentWeight(@PathVariable Long reptileId) {
        log.debug("REST request to get current weight for reptile: {}", reptileId);

        return weightLogService.getCurrentWeight(reptileId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Retrieves weight history for a reptile.
     * @param reptileId the reptile ID
     * @return list of weights in chronological order
     */
    @GetMapping("/reptile/{reptileId}/history")
    public ResponseEntity<List<BigDecimal>> getWeightHistory(@PathVariable Long reptileId) {
        log.debug("REST request to get weight history for reptile: {}", reptileId);

        List<BigDecimal> weightHistory = weightLogService.getWeightHistory(reptileId);
        return ResponseEntity.ok(weightHistory);
    }

    /**
     * Updates an existing weight log.
     * @param id the weight log ID
     * @param weightLogDto the updated weight log data
     * @return the updated weight log
     */
    @PutMapping("/{id}")
    public ResponseEntity<WeightLogDto> updateWeightLog(@PathVariable Long id, @Valid @RequestBody WeightLogDto weightLogDto) {
        log.info("REST request to update weight log: {}", id);

        return weightLogService.updateWeightLog(id, weightLogDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Deletes a weight log.
     * @param id the weight log ID
     * @return success status
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWeightLog(@PathVariable Long id) {
        log.info("REST request to delete weight log: {}", id);

        boolean success = weightLogService.deleteWeightLog(id);
        return success ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    /**
     * Gets weight statistics for a reptile.
     * @param reptileId the reptile ID
     * @return weight statistics
     */
    @GetMapping("/reptile/{reptileId}/statistics")
    public ResponseEntity<WeightLogService.WeightStatistics> getWeightStatistics(@PathVariable Long reptileId) {
        log.debug("REST request to get weight statistics for reptile: {}", reptileId);

        WeightLogService.WeightStatistics statistics = weightLogService.getWeightStatistics(reptileId);
        return ResponseEntity.ok(statistics);
    }
}
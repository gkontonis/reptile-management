package com.reptilemanagement.rest.controller;

import com.reptilemanagement.dto.FeedingLogDto;
import com.reptilemanagement.rest.service.FeedingLogService;
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
 * REST controller for managing feeding log operations.
 * Provides endpoints for CRUD operations on feeding logs.
 */
@RestController
@RequestMapping("/api/feeding-logs")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('USER')")
public class FeedingLogController {

    private final FeedingLogService feedingLogService;

    /**
     * Creates a new feeding log entry.
     * @param feedingLogDto the feeding log data
     * @return the created feeding log
     */
    @PostMapping
    public ResponseEntity<FeedingLogDto> createFeedingLog(@Valid @RequestBody FeedingLogDto feedingLogDto) {
        log.info("REST request to create feeding log for reptile: {}", feedingLogDto.getReptileId());

        FeedingLogDto result = feedingLogService.createFeedingLog(feedingLogDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    /**
     * Retrieves a feeding log by ID.
     * @param id the feeding log ID
     * @return the feeding log
     */
    @GetMapping("/{id}")
    public ResponseEntity<FeedingLogDto> getFeedingLog(@PathVariable Long id) {
        log.debug("REST request to get feeding log: {}", id);

        return feedingLogService.getFeedingLogById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Retrieves all feeding logs for a specific reptile.
     * @param reptileId the reptile ID
     * @return list of feeding logs for the reptile
     */
    @GetMapping("/reptile/{reptileId}")
    public ResponseEntity<List<FeedingLogDto>> getFeedingLogsByReptile(@PathVariable Long reptileId) {
        log.debug("REST request to get feeding logs for reptile: {}", reptileId);

        List<FeedingLogDto> feedingLogs = feedingLogService.getFeedingLogsByReptileId(reptileId);
        return ResponseEntity.ok(feedingLogs);
    }

    /**
     * Retrieves feeding logs for a reptile within a date range.
     * @param reptileId the reptile ID
     * @param startDate the start date
     * @param endDate the end date
     * @return list of feeding logs within the date range
     */
    @GetMapping("/reptile/{reptileId}/range")
    public ResponseEntity<List<FeedingLogDto>> getFeedingLogsByDateRange(
            @PathVariable Long reptileId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        log.debug("REST request to get feeding logs for reptile {} between {} and {}", reptileId, startDate, endDate);

        List<FeedingLogDto> feedingLogs = feedingLogService.getFeedingLogsByReptileIdAndDateRange(reptileId, startDate, endDate);
        return ResponseEntity.ok(feedingLogs);
    }

    /**
     * Retrieves the most recent feeding log for a reptile.
     * @param reptileId the reptile ID
     * @return the latest feeding log
     */
    @GetMapping("/reptile/{reptileId}/latest")
    public ResponseEntity<FeedingLogDto> getLatestFeedingLog(@PathVariable Long reptileId) {
        log.debug("REST request to get latest feeding log for reptile: {}", reptileId);

        return feedingLogService.getLatestFeedingLog(reptileId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Retrieves feeding logs where the reptile refused food.
     * @param reptileId the reptile ID
     * @return list of missed feeding logs
     */
    @GetMapping("/reptile/{reptileId}/missed")
    public ResponseEntity<List<FeedingLogDto>> getMissedFeedings(@PathVariable Long reptileId) {
        log.debug("REST request to get missed feedings for reptile: {}", reptileId);

        List<FeedingLogDto> feedingLogs = feedingLogService.getMissedFeedings(reptileId);
        return ResponseEntity.ok(feedingLogs);
    }

    /**
     * Updates an existing feeding log.
     * @param id the feeding log ID
     * @param feedingLogDto the updated feeding log data
     * @return the updated feeding log
     */
    @PutMapping("/{id}")
    public ResponseEntity<FeedingLogDto> updateFeedingLog(@PathVariable Long id, @Valid @RequestBody FeedingLogDto feedingLogDto) {
        log.info("REST request to update feeding log: {}", id);

        return feedingLogService.updateFeedingLog(id, feedingLogDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Deletes a feeding log.
     * @param id the feeding log ID
     * @return success status
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFeedingLog(@PathVariable Long id) {
        log.info("REST request to delete feeding log: {}", id);

        boolean success = feedingLogService.deleteFeedingLog(id);
        return success ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    /**
     * Gets feeding statistics for a reptile.
     * @param reptileId the reptile ID
     * @return feeding statistics
     */
    @GetMapping("/reptile/{reptileId}/statistics")
    public ResponseEntity<FeedingLogService.FeedingStatistics> getFeedingStatistics(@PathVariable Long reptileId) {
        log.debug("REST request to get feeding statistics for reptile: {}", reptileId);

        FeedingLogService.FeedingStatistics statistics = feedingLogService.getFeedingStatistics(reptileId);
        return ResponseEntity.ok(statistics);
    }
}
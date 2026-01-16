package com.reptilemanagement.rest.service;

import com.reptilemanagement.persistence.domain.FeedingLog;
import com.reptilemanagement.persistence.dto.FeedingLogDto;
import com.reptilemanagement.persistence.mapper.FeedingLogMapper;
import com.reptilemanagement.persistence.repository.FeedingLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for managing feeding log operations.
 * Provides business logic for CRUD operations on feeding logs.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FeedingLogService {

    private final FeedingLogRepository feedingLogRepository;
    private final FeedingLogMapper feedingLogMapper;

    /**
     * Creates a new feeding log entry.
     * @param feedingLogDto the feeding log data to create
     * @return the created feeding log as DTO
     */
    public FeedingLogDto createFeedingLog(FeedingLogDto feedingLogDto) {
        log.info("Creating feeding log for reptile: {}", feedingLogDto.getReptileId());

        FeedingLog feedingLog = feedingLogMapper.toEntity(feedingLogDto);
        FeedingLog savedFeedingLog = feedingLogRepository.save(feedingLog);

        log.info("Created feeding log with ID: {}", savedFeedingLog.getId());
        return feedingLogMapper.toDto(savedFeedingLog);
    }

    /**
     * Retrieves a feeding log by ID.
     * @param id the feeding log ID
     * @return the feeding log as DTO, or empty if not found
     */
    @Transactional(readOnly = true)
    public Optional<FeedingLogDto> getFeedingLogById(Long id) {
        log.debug("Retrieving feeding log with ID: {}", id);

        return feedingLogRepository.findById(id)
                .map(feedingLogMapper::toDto);
    }

    /**
     * Retrieves all feeding logs for a specific reptile.
     * @param reptileId the reptile ID
     * @return list of feeding logs for the reptile
     */
    @Transactional(readOnly = true)
    public List<FeedingLogDto> getFeedingLogsByReptileId(Long reptileId) {
        log.debug("Retrieving feeding logs for reptile: {}", reptileId);

        return feedingLogRepository.findByReptileIdOrderByFeedingDateDesc(reptileId).stream()
                .map(feedingLogMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves feeding logs for a reptile within a date range.
     * @param reptileId the reptile ID
     * @param startDate the start date
     * @param endDate the end date
     * @return list of feeding logs within the date range
     */
    @Transactional(readOnly = true)
    public List<FeedingLogDto> getFeedingLogsByReptileIdAndDateRange(Long reptileId, LocalDateTime startDate, LocalDateTime endDate) {
        log.debug("Retrieving feeding logs for reptile {} between {} and {}", reptileId, startDate, endDate);

        return feedingLogRepository.findByReptileIdAndFeedingDateBetweenOrderByFeedingDateDesc(reptileId, startDate, endDate).stream()
                .map(feedingLogMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves the most recent feeding log for a reptile.
     * @param reptileId the reptile ID
     * @return the most recent feeding log, or empty if none exists
     */
    @Transactional(readOnly = true)
    public Optional<FeedingLogDto> getLatestFeedingLog(Long reptileId) {
        log.debug("Retrieving latest feeding log for reptile: {}", reptileId);

        return Optional.ofNullable(feedingLogRepository.findTopByReptileIdOrderByFeedingDateDesc(reptileId))
                .map(feedingLogMapper::toDto);
    }

    /**
     * Retrieves feeding logs where the reptile refused food.
     * @param reptileId the reptile ID
     * @return list of missed feeding logs
     */
    @Transactional(readOnly = true)
    public List<FeedingLogDto> getMissedFeedings(Long reptileId) {
        log.debug("Retrieving missed feedings for reptile: {}", reptileId);

        return feedingLogRepository.findMissedFeedingsByReptileId(reptileId).stream()
                .map(feedingLogMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Updates an existing feeding log.
     * @param id the feeding log ID to update
     * @param feedingLogDto the updated feeding log data
     * @return the updated feeding log as DTO, or empty if not found
     */
    public Optional<FeedingLogDto> updateFeedingLog(Long id, FeedingLogDto feedingLogDto) {
        log.info("Updating feeding log with ID: {}", id);

        return feedingLogRepository.findById(id)
                .map(existingLog -> {
                    FeedingLog updatedLog = feedingLogMapper.toEntity(feedingLogDto);
                    updatedLog.setId(id);
                    updatedLog.setCreatedAt(existingLog.getCreatedAt());
                    FeedingLog savedLog = feedingLogRepository.save(updatedLog);

                    log.info("Updated feeding log with ID: {}", id);
                    return feedingLogMapper.toDto(savedLog);
                });
    }

    /**
     * Deletes a feeding log by ID.
     * @param id the feeding log ID to delete
     * @return true if deleted, false if not found
     */
    public boolean deleteFeedingLog(Long id) {
        log.info("Deleting feeding log with ID: {}", id);

        if (feedingLogRepository.existsById(id)) {
            feedingLogRepository.deleteById(id);
            log.info("Deleted feeding log with ID: {}", id);
            return true;
        }

        log.warn("Feeding log with ID {} not found for deletion", id);
        return false;
    }

    /**
     * Gets feeding statistics for a reptile.
     * @param reptileId the reptile ID
     * @return statistics object with feeding information
     */
    @Transactional(readOnly = true)
    public FeedingStatistics getFeedingStatistics(Long reptileId) {
        log.debug("Retrieving feeding statistics for reptile: {}", reptileId);

        List<FeedingLog> logs = feedingLogRepository.findByReptileIdOrderByFeedingDateDesc(reptileId);
        long totalFeedings = logs.size();
        long missedFeedings = logs.stream().filter(log -> !log.getAte()).count();

        return new FeedingStatistics(totalFeedings, missedFeedings);
    }

    /**
     * Inner class for feeding statistics.
     */
    public static class FeedingStatistics {
        private final long totalFeedings;
        private final long missedFeedings;

        public FeedingStatistics(long totalFeedings, long missedFeedings) {
            this.totalFeedings = totalFeedings;
            this.missedFeedings = missedFeedings;
        }

        public long getTotalFeedings() { return totalFeedings; }
        public long getMissedFeedings() { return missedFeedings; }
        public double getFeedingSuccessRate() {
            return totalFeedings > 0 ? ((double) (totalFeedings - missedFeedings) / totalFeedings) * 100 : 0;
        }
    }
}
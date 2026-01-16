package com.reptilemanagement.rest.service;

import com.reptilemanagement.persistence.domain.SheddingLog;
import com.reptilemanagement.persistence.dto.SheddingLogDto;
import com.reptilemanagement.persistence.mapper.SheddingLogMapper;
import com.reptilemanagement.persistence.mapper.base.BaseMapper;
import com.reptilemanagement.persistence.repository.SheddingLogRepository;
import com.reptilemanagement.rest.service.base.BaseCrudService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for managing shedding log operations.
 * Provides business logic for CRUD operations on shedding logs.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SheddingLogService extends BaseCrudService<Long, SheddingLog, SheddingLogDto> {

    private final SheddingLogRepository sheddingLogRepository;
    private final SheddingLogMapper sheddingLogMapper;

    @Override
    protected JpaRepository<SheddingLog, Long> getRepository() {
        return sheddingLogRepository;
    }

    @Override
    protected BaseMapper<SheddingLog, SheddingLogDto> getMapper() {
        return sheddingLogMapper;
    }

    @Override
    public Sort getDefaultSort() {
        return Sort.by(Sort.Direction.DESC, "sheddingDate");
    }

    /**
     * Creates a new shedding log entry.
     * @param sheddingLogDto the shedding log data to create
     * @return the created shedding log as DTO
     */
    public SheddingLogDto createSheddingLog(SheddingLogDto sheddingLogDto) {
        log.info("Creating shedding log for reptile: {}", sheddingLogDto.getReptileId());
        return create(sheddingLogDto, new HashMap<>());
    }

    /**
     * Retrieves a shedding log by ID.
     * @param id the shedding log ID
     * @return the shedding log as DTO, or empty if not found
     */
    @Transactional(readOnly = true)
    public Optional<SheddingLogDto> getSheddingLogById(Long id) {
        log.debug("Retrieving shedding log with ID: {}", id);
        try {
            return Optional.of(findById(id, new HashMap<>()));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Retrieves all shedding logs for a specific reptile.
     * @param reptileId the reptile ID
     * @return list of shedding logs for the reptile
     */
    @Transactional(readOnly = true)
    public List<SheddingLogDto> getSheddingLogsByReptileId(Long reptileId) {
        log.debug("Retrieving shedding logs for reptile: {}", reptileId);

        return sheddingLogRepository.findByReptileIdOrderBySheddingDateDesc(reptileId).stream()
                .map(sheddingLogMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves shedding logs for a reptile within a date range.
     * @param reptileId the reptile ID
     * @param startDate the start date
     * @param endDate the end date
     * @return list of shedding logs within the date range
     */
    @Transactional(readOnly = true)
    public List<SheddingLogDto> getSheddingLogsByReptileIdAndDateRange(Long reptileId, LocalDateTime startDate, LocalDateTime endDate) {
        log.debug("Retrieving shedding logs for reptile {} between {} and {}", reptileId, startDate, endDate);

        return sheddingLogRepository.findByReptileIdAndSheddingDateBetweenOrderBySheddingDateDesc(reptileId, startDate, endDate).stream()
                .map(sheddingLogMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves the most recent shedding log for a reptile.
     * @param reptileId the reptile ID
     * @return the most recent shedding log, or empty if none exists
     */
    @Transactional(readOnly = true)
    public Optional<SheddingLogDto> getLatestSheddingLog(Long reptileId) {
        log.debug("Retrieving latest shedding log for reptile: {}", reptileId);

        return Optional.ofNullable(sheddingLogRepository.findTopByReptileIdOrderBySheddingDateDesc(reptileId))
                .map(sheddingLogMapper::toDto);
    }

    /**
     * Retrieves shedding logs where the reptile ate the shed skin.
     * @param reptileId the reptile ID
     * @return list of shedding logs where the reptile ate its shed
     */
    @Transactional(readOnly = true)
    public List<SheddingLogDto> getAteShedLogs(Long reptileId) {
        log.debug("Retrieving ate shed logs for reptile: {}", reptileId);

        return sheddingLogRepository.findAteShedByReptileId(reptileId).stream()
                .map(sheddingLogMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Updates an existing shedding log.
     * @param id the shedding log ID to update
     * @param sheddingLogDto the updated shedding log data
     * @return the updated shedding log as DTO, or empty if not found
     */
    public Optional<SheddingLogDto> updateSheddingLog(Long id, SheddingLogDto sheddingLogDto) {
        log.info("Updating shedding log with ID: {}", id);
        try {
            sheddingLogDto.setId(id);
            return Optional.of(update(sheddingLogDto, new HashMap<>()));
        } catch (Exception e) {
            log.error("Error updating shedding log with ID: {}", id, e);
            return Optional.empty();
        }
    }

    /**
     * Deletes a shedding log by ID.
     * @param id the shedding log ID to delete
     * @return true if deleted, false if not found
     */
    public boolean deleteSheddingLog(Long id) {
        log.info("Deleting shedding log with ID: {}", id);

        if (sheddingLogRepository.existsById(id)) {
            deleteById(id);
            log.info("Deleted shedding log with ID: {}", id);
            return true;
        }

        log.warn("Shedding log with ID {} not found for deletion", id);
        return false;
    }

    /**
     * Gets shedding statistics for a reptile.
     * @param reptileId the reptile ID
     * @return statistics object with shedding information
     */
    @Transactional(readOnly = true)
    public SheddingStatistics getSheddingStatistics(Long reptileId) {
        log.debug("Retrieving shedding statistics for reptile: {}", reptileId);

        List<SheddingLog> logs = sheddingLogRepository.findByReptileIdOrderBySheddingDateDesc(reptileId);
        long totalSheddings = logs.size();
        long ateShedCount = logs.stream().filter(log -> log.getAteShed() != null && log.getAteShed()).count();

        return new SheddingStatistics(totalSheddings, ateShedCount);
    }

    /**
     * Inner class for shedding statistics.
     */
    public static class SheddingStatistics {
        private final long totalSheddings;
        private final long ateShedCount;

        public SheddingStatistics(long totalSheddings, long ateShedCount) {
            this.totalSheddings = totalSheddings;
            this.ateShedCount = ateShedCount;
        }

        public long getTotalSheddings() { return totalSheddings; }
        public long getAteShedCount() { return ateShedCount; }
        public double getAteShedPercentage() {
            return totalSheddings > 0 ? ((double) ateShedCount / totalSheddings) * 100 : 0;
        }
    }
}
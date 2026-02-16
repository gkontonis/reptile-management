package com.reptilemanagement.rest.service;

import com.reptilemanagement.persistence.domain.PoopLog;
import com.reptilemanagement.persistence.dto.PoopLogDto;
import com.reptilemanagement.persistence.mapper.PoopLogMapper;
import com.reptilemanagement.persistence.mapper.base.BaseMapper;
import com.reptilemanagement.persistence.repository.PoopLogRepository;
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
 * Service class for managing poop log operations.
 * Provides business logic for CRUD operations on poop logs.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PoopLogService extends BaseCrudService<Long, PoopLog, PoopLogDto> {

    private final PoopLogRepository poopLogRepository;
    private final PoopLogMapper poopLogMapper;

    @Override
    protected JpaRepository<PoopLog, Long> getRepository() {
        return poopLogRepository;
    }

    @Override
    protected BaseMapper<PoopLog, PoopLogDto> getMapper() {
        return poopLogMapper;
    }

    @Override
    public Sort getDefaultSort() {
        return Sort.by(Sort.Direction.DESC, "poopDate");
    }

    /**
     * Creates a new poop log entry.
     * @param poopLogDto the poop log data to create
     * @return the created poop log as DTO
     */
    public PoopLogDto createPoopLog(PoopLogDto poopLogDto) {
        log.info("Creating poop log for reptile: {}", poopLogDto.getReptileId());
        return create(poopLogDto, new HashMap<>());
    }

    /**
     * Retrieves a poop log by ID.
     * @param id the poop log ID
     * @return the poop log as DTO, or empty if not found
     */
    @Transactional(readOnly = true)
    public Optional<PoopLogDto> getPoopLogById(Long id) {
        log.debug("Retrieving poop log with ID: {}", id);
        try {
            return Optional.of(findById(id, new HashMap<>()));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Retrieves all poop logs for a specific reptile.
     * @param reptileId the reptile ID
     * @return list of poop logs for the reptile
     */
    @Transactional(readOnly = true)
    public List<PoopLogDto> getPoopLogsByReptileId(Long reptileId) {
        log.debug("Retrieving poop logs for reptile: {}", reptileId);

        return poopLogRepository.findByReptileIdOrderByPoopDateDesc(reptileId).stream()
                .map(poopLogMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves poop logs for a reptile within a date range.
     * @param reptileId the reptile ID
     * @param startDate the start date
     * @param endDate the end date
     * @return list of poop logs within the date range
     */
    @Transactional(readOnly = true)
    public List<PoopLogDto> getPoopLogsByReptileIdAndDateRange(Long reptileId, LocalDateTime startDate, LocalDateTime endDate) {
        log.debug("Retrieving poop logs for reptile {} between {} and {}", reptileId, startDate, endDate);

        return poopLogRepository.findByReptileIdAndPoopDateBetweenOrderByPoopDateDesc(reptileId, startDate, endDate).stream()
                .map(poopLogMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves the most recent poop log for a reptile.
     * @param reptileId the reptile ID
     * @return the most recent poop log, or empty if none exists
     */
    @Transactional(readOnly = true)
    public Optional<PoopLogDto> getLatestPoopLog(Long reptileId) {
        log.debug("Retrieving latest poop log for reptile: {}", reptileId);

        return Optional.ofNullable(poopLogRepository.findTopByReptileIdOrderByPoopDateDesc(reptileId))
                .map(poopLogMapper::toDto);
    }

    /**
     * Retrieves poop logs where parasites were observed.
     * @param reptileId the reptile ID
     * @return list of poop logs with parasites
     */
    @Transactional(readOnly = true)
    public List<PoopLogDto> getPoopLogsWithParasites(Long reptileId) {
        log.debug("Retrieving poop logs with parasites for reptile: {}", reptileId);

        return poopLogRepository.findWithParasitesByReptileId(reptileId).stream()
                .map(poopLogMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Updates an existing poop log.
     * @param id the poop log ID to update
     * @param poopLogDto the updated poop log data
     * @return the updated poop log as DTO, or empty if not found
     */
    public Optional<PoopLogDto> updatePoopLog(Long id, PoopLogDto poopLogDto) {
        log.info("Updating poop log with ID: {}", id);
        try {
            poopLogDto.setId(id);
            return Optional.of(update(poopLogDto, new HashMap<>()));
        } catch (Exception e) {
            log.error("Error updating poop log with ID: {}", id, e);
            return Optional.empty();
        }
    }

    /**
     * Deletes a poop log by ID.
     * @param id the poop log ID to delete
     * @return true if deleted, false if not found
     */
    public boolean deletePoopLog(Long id) {
        log.info("Deleting poop log with ID: {}", id);

        if (poopLogRepository.existsById(id)) {
            deleteById(id);
            log.info("Deleted poop log with ID: {}", id);
            return true;
        }

        log.warn("Poop log with ID {} not found for deletion", id);
        return false;
    }

    /**
     * Gets poop statistics for a reptile.
     * @param reptileId the reptile ID
     * @return statistics object with poop information
     */
    @Transactional(readOnly = true)
    public PoopStatistics getPoopStatistics(Long reptileId) {
        log.debug("Retrieving poop statistics for reptile: {}", reptileId);

        List<PoopLog> logs = poopLogRepository.findByReptileIdOrderByPoopDateDesc(reptileId);
        long totalLogs = logs.size();
        long parasiteCount = logs.stream().filter(PoopLog::getParasitesPresent).count();

        return new PoopStatistics(totalLogs, parasiteCount);
    }

    /**
     * Inner class for poop statistics.
     */
    public static class PoopStatistics {
        private final long totalLogs;
        private final long parasiteCount;

        public PoopStatistics(long totalLogs, long parasiteCount) {
            this.totalLogs = totalLogs;
            this.parasiteCount = parasiteCount;
        }

        public long getTotalLogs() { return totalLogs; }
        public long getParasiteCount() { return parasiteCount; }
    }
}

package com.reptilemanagement.rest.service;

import com.reptilemanagement.persistence.domain.WeightLog;
import com.reptilemanagement.persistence.dto.WeightLogDto;
import com.reptilemanagement.persistence.mapper.WeightLogMapper;
import com.reptilemanagement.persistence.repository.WeightLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for managing weight log operations.
 * Provides business logic for CRUD operations on weight logs.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class WeightLogService {

    private final WeightLogRepository weightLogRepository;
    private final WeightLogMapper weightLogMapper;

    /**
     * Creates a new weight log entry.
     * @param weightLogDto the weight log data to create
     * @return the created weight log as DTO
     */
    public WeightLogDto createWeightLog(WeightLogDto weightLogDto) {
        log.info("Creating weight log for reptile: {}", weightLogDto.getReptileId());

        WeightLog weightLog = weightLogMapper.toEntity(weightLogDto);
        WeightLog savedWeightLog = weightLogRepository.save(weightLog);

        log.info("Created weight log with ID: {}", savedWeightLog.getId());
        return weightLogMapper.toDto(savedWeightLog);
    }

    /**
     * Retrieves a weight log by ID.
     * @param id the weight log ID
     * @return the weight log as DTO, or empty if not found
     */
    @Transactional(readOnly = true)
    public Optional<WeightLogDto> getWeightLogById(Long id) {
        log.debug("Retrieving weight log with ID: {}", id);

        return weightLogRepository.findById(id)
                .map(weightLogMapper::toDto);
    }

    /**
     * Retrieves all weight logs for a specific reptile.
     * @param reptileId the reptile ID
     * @return list of weight logs for the reptile
     */
    @Transactional(readOnly = true)
    public List<WeightLogDto> getWeightLogsByReptileId(Long reptileId) {
        log.debug("Retrieving weight logs for reptile: {}", reptileId);

        return weightLogRepository.findByReptileIdOrderByMeasurementDateDesc(reptileId).stream()
                .map(weightLogMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves weight logs for a reptile within a date range.
     * @param reptileId the reptile ID
     * @param startDate the start date
     * @param endDate the end date
     * @return list of weight logs within the date range
     */
    @Transactional(readOnly = true)
    public List<WeightLogDto> getWeightLogsByReptileIdAndDateRange(Long reptileId, LocalDateTime startDate, LocalDateTime endDate) {
        log.debug("Retrieving weight logs for reptile {} between {} and {}", reptileId, startDate, endDate);

        return weightLogRepository.findByReptileIdAndMeasurementDateBetweenOrderByMeasurementDateDesc(reptileId, startDate, endDate).stream()
                .map(weightLogMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves the most recent weight log for a reptile.
     * @param reptileId the reptile ID
     * @return the most recent weight log, or empty if none exists
     */
    @Transactional(readOnly = true)
    public Optional<WeightLogDto> getLatestWeightLog(Long reptileId) {
        log.debug("Retrieving latest weight log for reptile: {}", reptileId);

        return Optional.ofNullable(weightLogRepository.findTopByReptileIdOrderByMeasurementDateDesc(reptileId))
                .map(weightLogMapper::toDto);
    }

    /**
     * Retrieves the current weight for a reptile.
     * @param reptileId the reptile ID
     * @return the current weight in grams, or empty if no measurements exist
     */
    @Transactional(readOnly = true)
    public Optional<BigDecimal> getCurrentWeight(Long reptileId) {
        log.debug("Retrieving current weight for reptile: {}", reptileId);

        return getLatestWeightLog(reptileId)
                .map(WeightLogDto::getWeightGrams);
    }

    /**
     * Retrieves weight history for a reptile.
     * @param reptileId the reptile ID
     * @return list of weights in chronological order (oldest first)
     */
    @Transactional(readOnly = true)
    public List<BigDecimal> getWeightHistory(Long reptileId) {
        log.debug("Retrieving weight history for reptile: {}", reptileId);

        return weightLogRepository.findWeightHistoryByReptileId(reptileId).stream()
                .sorted() // Sort by measurement date (oldest first)
                .collect(Collectors.toList());
    }

    /**
     * Updates an existing weight log.
     * @param id the weight log ID to update
     * @param weightLogDto the updated weight log data
     * @return the updated weight log as DTO, or empty if not found
     */
    public Optional<WeightLogDto> updateWeightLog(Long id, WeightLogDto weightLogDto) {
        log.info("Updating weight log with ID: {}", id);

        return weightLogRepository.findById(id)
                .map(existingLog -> {
                    WeightLog updatedLog = weightLogMapper.toEntity(weightLogDto);
                    updatedLog.setId(id);
                    updatedLog.setCreatedAt(existingLog.getCreatedAt());
                    WeightLog savedLog = weightLogRepository.save(updatedLog);

                    log.info("Updated weight log with ID: {}", id);
                    return weightLogMapper.toDto(savedLog);
                });
    }

    /**
     * Deletes a weight log by ID.
     * @param id the weight log ID to delete
     * @return true if deleted, false if not found
     */
    public boolean deleteWeightLog(Long id) {
        log.info("Deleting weight log with ID: {}", id);

        if (weightLogRepository.existsById(id)) {
            weightLogRepository.deleteById(id);
            log.info("Deleted weight log with ID: {}", id);
            return true;
        }

        log.warn("Weight log with ID {} not found for deletion", id);
        return false;
    }

    /**
     * Gets weight statistics for a reptile.
     * @param reptileId the reptile ID
     * @return statistics object with weight information
     */
    @Transactional(readOnly = true)
    public WeightStatistics getWeightStatistics(Long reptileId) {
        log.debug("Retrieving weight statistics for reptile: {}", reptileId);

        List<BigDecimal> weights = getWeightHistory(reptileId);
        if (weights.isEmpty()) {
            return new WeightStatistics(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, 0);
        }

        BigDecimal currentWeight = weights.get(weights.size() - 1);
        BigDecimal initialWeight = weights.get(0);
        BigDecimal weightGain = currentWeight.subtract(initialWeight);
        int measurementCount = weights.size();

        return new WeightStatistics(currentWeight, initialWeight, weightGain, measurementCount);
    }

    /**
     * Inner class for weight statistics.
     */
    public static class WeightStatistics {
        private final BigDecimal currentWeight;
        private final BigDecimal initialWeight;
        private final BigDecimal weightGain;
        private final int measurementCount;

        public WeightStatistics(BigDecimal currentWeight, BigDecimal initialWeight, BigDecimal weightGain, int measurementCount) {
            this.currentWeight = currentWeight;
            this.initialWeight = initialWeight;
            this.weightGain = weightGain;
            this.measurementCount = measurementCount;
        }

        public BigDecimal getCurrentWeight() { return currentWeight; }
        public BigDecimal getInitialWeight() { return initialWeight; }
        public BigDecimal getWeightGain() { return weightGain; }
        public int getMeasurementCount() { return measurementCount; }
    }
}
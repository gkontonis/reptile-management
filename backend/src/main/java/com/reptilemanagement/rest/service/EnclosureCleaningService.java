package com.reptilemanagement.rest.service;

import com.reptilemanagement.persistence.domain.EnclosureCleaning;
import com.reptilemanagement.persistence.dto.EnclosureCleaningDto;
import com.reptilemanagement.persistence.mapper.EnclosureCleaningMapper;
import com.reptilemanagement.persistence.mapper.base.BaseMapper;
import com.reptilemanagement.persistence.repository.EnclosureCleaningRepository;
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
 * Service class for managing enclosure cleaning operations.
 * Provides business logic for CRUD operations on enclosure cleanings.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EnclosureCleaningService extends BaseCrudService<Long, EnclosureCleaning, EnclosureCleaningDto> {

    private final EnclosureCleaningRepository enclosureCleaningRepository;
    private final EnclosureCleaningMapper enclosureCleaningMapper;

    @Override
    protected JpaRepository<EnclosureCleaning, Long> getRepository() {
        return enclosureCleaningRepository;
    }

    @Override
    protected BaseMapper<EnclosureCleaning, EnclosureCleaningDto> getMapper() {
        return enclosureCleaningMapper;
    }

    @Override
    public Sort getDefaultSort() {
        return Sort.by(Sort.Direction.DESC, "cleaningDate");
    }

    /**
     * Creates a new enclosure cleaning log entry.
     * @param enclosureCleaningDto the enclosure cleaning data to create
     * @return the created enclosure cleaning as DTO
     */
    public EnclosureCleaningDto createEnclosureCleaning(EnclosureCleaningDto enclosureCleaningDto) {
        log.info("Creating enclosure cleaning log for enclosure: {}", enclosureCleaningDto.getEnclosureId());
        return create(enclosureCleaningDto, new HashMap<>());
    }

    /**
     * Retrieves an enclosure cleaning log by ID.
     * @param id the enclosure cleaning log ID
     * @return the enclosure cleaning log as DTO, or empty if not found
     */
    @Transactional(readOnly = true)
    public Optional<EnclosureCleaningDto> getEnclosureCleaningById(Long id) {
        log.debug("Retrieving enclosure cleaning log with ID: {}", id);
        try {
            return Optional.of(findById(id, new HashMap<>()));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Retrieves all enclosure cleaning logs for a specific enclosure.
     * @param enclosureId the enclosure ID
     * @return list of enclosure cleaning logs for the enclosure
     */
    @Transactional(readOnly = true)
    public List<EnclosureCleaningDto> getEnclosureCleaningsByEnclosureId(Long enclosureId) {
        log.debug("Retrieving enclosure cleaning logs for enclosure: {}", enclosureId);

        return enclosureCleaningRepository.findByEnclosureIdOrderByCleaningDateDesc(enclosureId).stream()
                .map(enclosureCleaningMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves enclosure cleaning logs for an enclosure within a date range.
     * @param enclosureId the enclosure ID
     * @param startDate the start date
     * @param endDate the end date
     * @return list of enclosure cleaning logs within the date range
     */
    @Transactional(readOnly = true)
    public List<EnclosureCleaningDto> getEnclosureCleaningsByEnclosureIdAndDateRange(Long enclosureId, LocalDateTime startDate, LocalDateTime endDate) {
        log.debug("Retrieving enclosure cleaning logs for enclosure {} between {} and {}", enclosureId, startDate, endDate);

        return enclosureCleaningRepository.findByEnclosureIdAndCleaningDateBetweenOrderByCleaningDateDesc(enclosureId, startDate, endDate).stream()
                .map(enclosureCleaningMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves the most recent enclosure cleaning log for an enclosure.
     * @param enclosureId the enclosure ID
     * @return the most recent enclosure cleaning log, or empty if none exists
     */
    @Transactional(readOnly = true)
    public Optional<EnclosureCleaningDto> getLatestEnclosureCleaning(Long enclosureId) {
        log.debug("Retrieving latest enclosure cleaning log for enclosure: {}", enclosureId);

        return Optional.ofNullable(enclosureCleaningRepository.findTopByEnclosureIdOrderByCleaningDateDesc(enclosureId))
                .map(enclosureCleaningMapper::toDto);
    }

    /**
     * Retrieves enclosure cleaning logs where disinfection was performed.
     * @param enclosureId the enclosure ID
     * @return list of enclosure cleaning logs where disinfection occurred
     */
    @Transactional(readOnly = true)
    public List<EnclosureCleaningDto> getDisinfections(Long enclosureId) {
        log.debug("Retrieving disinfection logs for enclosure: {}", enclosureId);

        return enclosureCleaningRepository.findDisinfectionsByEnclosureId(enclosureId).stream()
                .map(enclosureCleaningMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves enclosure cleaning logs where substrate was changed.
     * @param enclosureId the enclosure ID
     * @return list of enclosure cleaning logs where substrate was changed
     */
    @Transactional(readOnly = true)
    public List<EnclosureCleaningDto> getSubstrateChanges(Long enclosureId) {
        log.debug("Retrieving substrate change logs for enclosure: {}", enclosureId);

        return enclosureCleaningRepository.findSubstrateChangesByEnclosureId(enclosureId).stream()
                .map(enclosureCleaningMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Updates an existing enclosure cleaning log.
     * @param id the enclosure cleaning log ID to update
     * @param enclosureCleaningDto the updated enclosure cleaning data
     * @return the updated enclosure cleaning log as DTO, or empty if not found
     */
    public Optional<EnclosureCleaningDto> updateEnclosureCleaning(Long id, EnclosureCleaningDto enclosureCleaningDto) {
        log.info("Updating enclosure cleaning log with ID: {}", id);

        return enclosureCleaningRepository.findById(id)
                .map(existingLog -> {
                    EnclosureCleaning updatedLog = enclosureCleaningMapper.toEntity(enclosureCleaningDto);
                    updatedLog.setId(id);
                    updatedLog.setCreatedAt(existingLog.getCreatedAt());
                    EnclosureCleaning savedLog = enclosureCleaningRepository.save(updatedLog);

                    log.info("Updated enclosure cleaning log with ID: {}", id);
                    return enclosureCleaningMapper.toDto(savedLog);
                });
    }

    /**
     * Deletes an enclosure cleaning log by ID.
     * @param id the enclosure cleaning log ID to delete
     * @return true if deleted, false if not found
     */
    public boolean deleteEnclosureCleaning(Long id) {
        log.info("Deleting enclosure cleaning log with ID: {}", id);

        if (enclosureCleaningRepository.existsById(id)) {
            enclosureCleaningRepository.deleteById(id);
            log.info("Deleted enclosure cleaning log with ID: {}", id);
            return true;
        }

        log.warn("Enclosure cleaning log with ID {} not found for deletion", id);
        return false;
    }

    /**
     * Gets enclosure cleaning statistics for an enclosure.
     * @param enclosureId the enclosure ID
     * @return statistics object with cleaning information
     */
    @Transactional(readOnly = true)
    public EnclosureCleaningStatistics getCleaningStatistics(Long enclosureId) {
        log.debug("Retrieving enclosure cleaning statistics for enclosure: {}", enclosureId);

        List<EnclosureCleaning> logs = enclosureCleaningRepository.findByEnclosureIdOrderByCleaningDateDesc(enclosureId);
        long totalCleanings = logs.size();
        long disinfections = logs.stream().filter(log -> log.getDisinfected()).count();
        long substrateChanges = logs.stream().filter(log -> log.getSubstrateChanged()).count();

        return new EnclosureCleaningStatistics(totalCleanings, disinfections, substrateChanges);
    }

    /**
     * Inner class for enclosure cleaning statistics.
     */
    public static class EnclosureCleaningStatistics {
        private final long totalCleanings;
        private final long disinfections;
        private final long substrateChanges;

        public EnclosureCleaningStatistics(long totalCleanings, long disinfections, long substrateChanges) {
            this.totalCleanings = totalCleanings;
            this.disinfections = disinfections;
            this.substrateChanges = substrateChanges;
        }

        public long getTotalCleanings() { return totalCleanings; }
        public long getDisinfections() { return disinfections; }
        public long getSubstrateChanges() { return substrateChanges; }
        public double getDisinfectionRate() {
            return totalCleanings > 0 ? ((double) disinfections / totalCleanings) * 100 : 0;
        }
        public double getSubstrateChangeRate() {
            return totalCleanings > 0 ? ((double) substrateChanges / totalCleanings) * 100 : 0;
        }
    }
}
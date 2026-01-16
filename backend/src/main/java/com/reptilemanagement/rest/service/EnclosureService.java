package com.reptilemanagement.rest.service;

import com.reptilemanagement.persistence.domain.Enclosure;
import com.reptilemanagement.persistence.dto.EnclosureDto;
import com.reptilemanagement.persistence.mapper.EnclosureMapper;
import com.reptilemanagement.persistence.mapper.base.BaseMapper;
import com.reptilemanagement.persistence.repository.EnclosureRepository;
import com.reptilemanagement.rest.service.base.BaseCrudService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for managing enclosure operations.
 * Provides business logic for CRUD operations on enclosures.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EnclosureService extends BaseCrudService<Long, Enclosure, EnclosureDto> {

    private final EnclosureRepository enclosureRepository;
    private final EnclosureMapper enclosureMapper;

    @Override
    protected JpaRepository<Enclosure, Long> getRepository() {
        return enclosureRepository;
    }

    @Override
    protected BaseMapper<Enclosure, EnclosureDto> getMapper() {
        return enclosureMapper;
    }

    @Override
    public Sort getDefaultSort() {
        return Sort.by(Sort.Direction.ASC, "name");
    }

    /**
     * Creates a new enclosure.
     * @param enclosureDto the enclosure data to create
     * @return the created enclosure as DTO
     */
    public EnclosureDto createEnclosure(EnclosureDto enclosureDto) {
        log.info("Creating new enclosure: {}", enclosureDto.getName());
        return create(enclosureDto, new HashMap<>());
    }

    /**
     * Retrieves an enclosure by ID.
     * @param id the enclosure ID
     * @return the enclosure as DTO, or empty if not found
     */
    @Transactional(readOnly = true)
    public Optional<EnclosureDto> getEnclosureById(Long id) {
        log.debug("Retrieving enclosure with ID: {}", id);
        try {
            return Optional.of(findById(id, new HashMap<>()));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Retrieves all enclosures.
     * @return list of all enclosures as DTOs
     */
    @Transactional(readOnly = true)
    public List<EnclosureDto> getAllEnclosures() {
        log.debug("Retrieving all enclosures");

        return enclosureRepository.findAll().stream()
                .map(enclosureMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves enclosures by type.
     * @param type the enclosure type
     * @return list of enclosures of the specified type
     */
    @Transactional(readOnly = true)
    public List<EnclosureDto> getEnclosuresByType(Enclosure.EnclosureType type) {
        log.debug("Retrieving enclosures by type: {}", type);

        return enclosureRepository.findByType(type).stream()
                .map(enclosureMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves enclosures by name.
     * @param name the name to search for
     * @return list of enclosures with names containing the search term
     */
    @Transactional(readOnly = true)
    public List<EnclosureDto> getEnclosuresByName(String name) {
        log.debug("Retrieving enclosures by name: {}", name);

        return enclosureRepository.findByNameContainingIgnoreCase(name).stream()
                .map(enclosureMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves enclosures that are currently occupied by reptiles.
     * @return list of occupied enclosure IDs
     */
    @Transactional(readOnly = true)
    public List<Long> getOccupiedEnclosureIds() {
        log.debug("Retrieving occupied enclosure IDs");

        return enclosureRepository.findOccupiedEnclosureIds();
    }

    /**
     * Retrieves enclosures that are currently empty.
     * @return list of empty enclosures as DTOs
     */
    @Transactional(readOnly = true)
    public List<EnclosureDto> getEmptyEnclosures() {
        log.debug("Retrieving empty enclosures");

        return enclosureRepository.findEmptyEnclosures().stream()
                .map(enclosureMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Updates an existing enclosure.
     * @param id the enclosure ID to update
     * @param enclosureDto the updated enclosure data
     * @return the updated enclosure as DTO, or empty if not found
     */
    public Optional<EnclosureDto> updateEnclosure(Long id, EnclosureDto enclosureDto) {
        log.info("Updating enclosure with ID: {}", id);
        try {
            enclosureDto.setId(id);
            return Optional.of(update(enclosureDto, new HashMap<>()));
        } catch (Exception e) {
            log.error("Error updating enclosure with ID: {}", id, e);
            return Optional.empty();
        }
    }

    /**
     * Deletes an enclosure by ID.
     * @param id the enclosure ID to delete
     * @return true if deleted, false if not found
     */
    public boolean deleteEnclosure(Long id) {
        log.info("Deleting enclosure with ID: {}", id);

        if (enclosureRepository.existsById(id)) {
            deleteById(id);
            log.info("Deleted enclosure with ID: {}", id);
            return true;
        }

        log.warn("Enclosure with ID {} not found for deletion", id);
        return false;
    }

    /**
     * Checks if an enclosure can be safely deleted (no reptiles assigned).
     * @param enclosureId the enclosure ID to check
     * @return true if the enclosure is empty and can be deleted
     */
    @Transactional(readOnly = true)
    public boolean canDeleteEnclosure(Long enclosureId) {
        log.debug("Checking if enclosure {} can be deleted", enclosureId);

        List<Long> occupiedIds = getOccupiedEnclosureIds();
        return !occupiedIds.contains(enclosureId);
    }

    /**
     * Gets statistics about enclosures.
     * @return statistics object with enclosure information
     */
    @Transactional(readOnly = true)
    public EnclosureStatistics getStatistics() {
        log.debug("Retrieving enclosure statistics");

        long totalCount = enclosureRepository.count();
        long terrariumCount = enclosureRepository.countByType(Enclosure.EnclosureType.TERRARIUM);
        long vivariumCount = enclosureRepository.countByType(Enclosure.EnclosureType.VIVARIUM);
        long occupiedCount = getOccupiedEnclosureIds().size();
        long emptyCount = enclosureRepository.findEmptyEnclosures().size();

        return new EnclosureStatistics(totalCount, terrariumCount, vivariumCount, occupiedCount, emptyCount);
    }

    /**
     * Inner class for enclosure statistics.
     */
    public static class EnclosureStatistics {
        private final long total;
        private final long terrariums;
        private final long vivariums;
        private final long occupied;
        private final long empty;

        public EnclosureStatistics(long total, long terrariums, long vivariums, long occupied, long empty) {
            this.total = total;
            this.terrariums = terrariums;
            this.vivariums = vivariums;
            this.occupied = occupied;
            this.empty = empty;
        }

        public long getTotal() { return total; }
        public long getTerrariums() { return terrariums; }
        public long getVivariums() { return vivariums; }
        public long getOccupied() { return occupied; }
        public long getEmpty() { return empty; }
        public double getOccupancyRate() {
            return total > 0 ? ((double) occupied / total) * 100 : 0;
        }
    }
}
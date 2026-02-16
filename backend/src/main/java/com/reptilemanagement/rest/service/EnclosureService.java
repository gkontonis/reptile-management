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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for managing enclosure operations.
 * All operations are scoped to the currently authenticated user.
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

    // ==================== Helper ====================

    private Long currentUserId() {
        return authenticationInformationProvider.getAuthenticatedUserId();
    }

    /**
     * Verifies that the given enclosure belongs to the current user.
     * @param enclosureId the enclosure ID to check
     * @throws AccessDeniedException if the enclosure does not belong to the current user
     */
    public void verifyOwnership(Long enclosureId) {
        Long userId = currentUserId();
        if (!enclosureRepository.existsByIdAndUserId(enclosureId, userId)) {
            throw new AccessDeniedException("Enclosure does not belong to the current user");
        }
    }

    // ==================== CRUD ====================

    /**
     * Creates a new enclosure owned by the current user.
     * @param enclosureDto the enclosure data to create
     * @return the created enclosure as DTO
     */
    public EnclosureDto createEnclosure(EnclosureDto enclosureDto) {
        log.info("Creating new enclosure: {}", enclosureDto.getName());
        enclosureDto.setUserId(currentUserId());
        return create(enclosureDto, new HashMap<>());
    }

    /**
     * Retrieves an enclosure by ID, scoped to the current user.
     * @param id the enclosure ID
     * @return the enclosure as DTO, or empty if not found or not owned
     */
    @Transactional(readOnly = true)
    public Optional<EnclosureDto> getEnclosureById(Long id) {
        log.debug("Retrieving enclosure with ID: {}", id);
        return enclosureRepository.findByIdAndUserId(id, currentUserId())
                .map(enclosureMapper::toDto);
    }

    /**
     * Retrieves all enclosures for the current user.
     * @return list of the user's enclosures as DTOs
     */
    @Transactional(readOnly = true)
    public List<EnclosureDto> getAllEnclosures() {
        log.debug("Retrieving all enclosures for current user");
        return enclosureRepository.findByUserId(currentUserId()).stream()
                .map(enclosureMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves enclosures by type for the current user.
     * @param type the enclosure type
     * @return list of enclosures of the specified type
     */
    @Transactional(readOnly = true)
    public List<EnclosureDto> getEnclosuresByType(Enclosure.EnclosureType type) {
        log.debug("Retrieving enclosures by type: {}", type);
        return enclosureRepository.findByUserIdAndType(currentUserId(), type).stream()
                .map(enclosureMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves enclosures by name for the current user.
     * @param name the name to search for
     * @return list of enclosures with names containing the search term
     */
    @Transactional(readOnly = true)
    public List<EnclosureDto> getEnclosuresByName(String name) {
        log.debug("Retrieving enclosures by name: {}", name);
        return enclosureRepository.findByUserIdAndNameContainingIgnoreCase(currentUserId(), name).stream()
                .map(enclosureMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves occupied enclosure IDs for the current user.
     * @return list of occupied enclosure IDs
     */
    @Transactional(readOnly = true)
    public List<Long> getOccupiedEnclosureIds() {
        log.debug("Retrieving occupied enclosure IDs for current user");
        return enclosureRepository.findOccupiedEnclosureIdsByUserId(currentUserId());
    }

    /**
     * Retrieves empty enclosures for the current user.
     * @return list of empty enclosures as DTOs
     */
    @Transactional(readOnly = true)
    public List<EnclosureDto> getEmptyEnclosures() {
        log.debug("Retrieving empty enclosures for current user");
        return enclosureRepository.findEmptyEnclosuresByUserId(currentUserId()).stream()
                .map(enclosureMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Updates an enclosure, verifying ownership first.
     * @param id the enclosure ID to update
     * @param enclosureDto the updated enclosure data
     * @return the updated enclosure as DTO, or empty if not found
     */
    public Optional<EnclosureDto> updateEnclosure(Long id, EnclosureDto enclosureDto) {
        log.info("Updating enclosure with ID: {}", id);
        Long userId = currentUserId();

        return enclosureRepository.findByIdAndUserId(id, userId)
                .map(existing -> {
                    try {
                        enclosureDto.setId(id);
                        enclosureDto.setUserId(userId);
                        return Optional.of(update(enclosureDto, new HashMap<>()));
                    } catch (Exception e) {
                        log.error("Error updating enclosure with ID: {}", id, e);
                        return Optional.<EnclosureDto>empty();
                    }
                })
                .orElse(Optional.empty());
    }

    /**
     * Deletes an enclosure, verifying ownership first.
     * @param id the enclosure ID to delete
     * @return true if deleted, false if not found or not owned
     */
    public boolean deleteEnclosure(Long id) {
        log.info("Deleting enclosure with ID: {}", id);
        Long userId = currentUserId();

        if (enclosureRepository.existsByIdAndUserId(id, userId)) {
            deleteById(id);
            log.info("Deleted enclosure with ID: {}", id);
            return true;
        }

        log.warn("Enclosure with ID {} not found for current user", id);
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
     * Gets statistics about enclosures for the current user.
     * @return statistics object with enclosure information
     */
    @Transactional(readOnly = true)
    public EnclosureStatistics getStatistics() {
        log.debug("Retrieving enclosure statistics for current user");
        Long userId = currentUserId();

        long totalCount = enclosureRepository.countByUserId(userId);
        long terrariumCount = enclosureRepository.countByUserIdAndType(userId, Enclosure.EnclosureType.TERRARIUM);
        long vivariumCount = enclosureRepository.countByUserIdAndType(userId, Enclosure.EnclosureType.VIVARIUM);
        long occupiedCount = getOccupiedEnclosureIds().size();
        long emptyCount = enclosureRepository.findEmptyEnclosuresByUserId(userId).size();

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
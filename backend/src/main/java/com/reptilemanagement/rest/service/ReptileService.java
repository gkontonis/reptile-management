package com.reptilemanagement.rest.service;

import com.reptilemanagement.persistence.domain.Reptile;
import com.reptilemanagement.persistence.domain.ReptileImage;
import com.reptilemanagement.persistence.dto.ReptileDto;
import com.reptilemanagement.persistence.mapper.ReptileMapper;
import com.reptilemanagement.persistence.mapper.base.BaseMapper;
import com.reptilemanagement.persistence.repository.ReptileImageRepository;
import com.reptilemanagement.persistence.repository.ReptileRepository;
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
 * Service class for managing reptile operations.
 * All operations are scoped to the currently authenticated user.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ReptileService extends BaseCrudService<Long, Reptile, ReptileDto> {

    private final ReptileRepository reptileRepository;
    private final ReptileImageRepository reptileImageRepository;
    private final ReptileMapper reptileMapper;

    @Override
    protected JpaRepository<Reptile, Long> getRepository() {
        return reptileRepository;
    }

    @Override
    protected BaseMapper<Reptile, ReptileDto> getMapper() {
        return reptileMapper;
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
     * Verifies that the given reptile belongs to the current user.
     * @param reptileId the reptile ID to check
     * @throws AccessDeniedException if the reptile does not belong to the current user
     */
    public void verifyOwnership(Long reptileId) {
        Long userId = currentUserId();
        if (!reptileRepository.existsByIdAndUserId(reptileId, userId)) {
            throw new AccessDeniedException("Reptile does not belong to the current user");
        }
    }

    // ==================== CRUD ====================

    /**
     * Creates a new reptile owned by the current user.
     * @param reptileDto the reptile data to create
     * @return the created reptile as DTO
     */
    public ReptileDto createReptile(ReptileDto reptileDto) {
        log.info("Creating new reptile: {}", reptileDto.getName());
        reptileDto.setUserId(currentUserId());
        return create(reptileDto, new HashMap<>());
    }

    /**
     * Retrieves a reptile by ID, scoped to the current user.
     * @param id the reptile ID
     * @return the reptile as DTO, or empty if not found or not owned
     */
    @Transactional(readOnly = true)
    public Optional<ReptileDto> getReptileById(Long id) {
        log.debug("Retrieving reptile with ID: {}", id);
        return reptileRepository.findByIdAndUserId(id, currentUserId())
                .map(reptileMapper::toDto);
    }

    /**
     * Retrieves all reptiles for the current user.
     * @return list of the user's reptiles as DTOs
     */
    @Transactional(readOnly = true)
    public List<ReptileDto> getAllReptiles() {
        log.debug("Retrieving all reptiles for current user");
        return reptileRepository.findByUserId(currentUserId()).stream()
                .map(reptileMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all active reptiles for the current user.
     * @return list of active reptiles as DTOs
     */
    @Transactional(readOnly = true)
    public List<ReptileDto> getActiveReptiles() {
        log.debug("Retrieving active reptiles for current user");
        return reptileRepository.findByUserIdAndStatus(currentUserId(), Reptile.ReptileStatus.ACTIVE).stream()
                .map(reptileMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves reptiles by species for the current user.
     * @param species the species to search for
     * @return list of matching reptiles
     */
    @Transactional(readOnly = true)
    public List<ReptileDto> getReptilesBySpecies(String species) {
        log.debug("Retrieving reptiles by species: {}", species);
        return reptileRepository.findByUserIdAndSpeciesContainingIgnoreCase(currentUserId(), species).stream()
                .map(reptileMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves reptiles by name for the current user.
     * @param name the name to search for
     * @return list of matching reptiles
     */
    @Transactional(readOnly = true)
    public List<ReptileDto> getReptilesByName(String name) {
        log.debug("Retrieving reptiles by name: {}", name);
        return reptileRepository.findByUserIdAndNameContainingIgnoreCase(currentUserId(), name).stream()
                .map(reptileMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves reptiles in a specific enclosure for the current user.
     * @param enclosureId the enclosure ID
     * @return list of reptiles in the specified enclosure
     */
    @Transactional(readOnly = true)
    public List<ReptileDto> getReptilesByEnclosure(Long enclosureId) {
        log.debug("Retrieving reptiles in enclosure: {}", enclosureId);
        return reptileRepository.findByUserIdAndEnclosureId(currentUserId(), enclosureId).stream()
                .map(reptileMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Updates a reptile, verifying ownership first.
     * @param id the reptile ID to update
     * @param reptileDto the updated reptile data
     * @return the updated reptile as DTO, or empty if not found
     */
    public Optional<ReptileDto> updateReptile(Long id, ReptileDto reptileDto) {
        log.info("Updating reptile with ID: {}", id);
        Long userId = currentUserId();

        return reptileRepository.findByIdAndUserId(id, userId)
                .map(existing -> {
                    try {
                        reptileDto.setId(id);
                        reptileDto.setUserId(userId);
                        return Optional.of(update(reptileDto, new HashMap<>()));
                    } catch (Exception e) {
                        log.error("Error updating reptile with ID: {}", id, e);
                        return Optional.<ReptileDto>empty();
                    }
                })
                .orElse(Optional.empty());
    }

    /**
     * Deletes a reptile, verifying ownership first.
     * @param id the reptile ID to delete
     * @return true if deleted, false if not found or not owned
     */
    public boolean deleteReptile(Long id) {
        log.info("Deleting reptile with ID: {}", id);
        Long userId = currentUserId();

        if (reptileRepository.existsByIdAndUserId(id, userId)) {
            deleteById(id);
            log.info("Deleted reptile with ID: {}", id);
            return true;
        }

        log.warn("Reptile with ID {} not found for current user", id);
        return false;
    }

    /**
     * Moves a reptile to a different enclosure, verifying ownership.
     * @param reptileId the reptile ID
     * @param enclosureId the new enclosure ID (null to remove from enclosure)
     * @return true if moved successfully
     */
    public boolean moveReptileToEnclosure(Long reptileId, Long enclosureId) {
        log.info("Moving reptile {} to enclosure {}", reptileId, enclosureId);
        Long userId = currentUserId();

        return reptileRepository.findByIdAndUserId(reptileId, userId)
                .map(reptile -> {
                    reptile.setEnclosureId(enclosureId);
                    reptileRepository.save(reptile);
                    log.info("Moved reptile {} to enclosure {}", reptileId, enclosureId);
                    return true;
                })
                .orElse(false);
    }

    /**
     * Updates the status of a reptile, verifying ownership.
     * @param reptileId the reptile ID
     * @param status the new status
     * @return true if updated successfully
     */
    public boolean updateReptileStatus(Long reptileId, Reptile.ReptileStatus status) {
        log.info("Updating reptile {} status to {}", reptileId, status);
        Long userId = currentUserId();

        return reptileRepository.findByIdAndUserId(reptileId, userId)
                .map(reptile -> {
                    reptile.setStatus(status);
                    reptileRepository.save(reptile);
                    log.info("Updated reptile {} status to {}", reptileId, status);
                    return true;
                })
                .orElse(false);
    }

    /**
     * Sets the highlight image for a reptile, verifying ownership.
     * @param reptileId the reptile ID
     * @param imageId the image ID to set as highlight
     * @return the updated reptile as DTO
     */
    public Optional<ReptileDto> setHighlightImage(Long reptileId, Long imageId) {
        log.info("Setting highlight image {} for reptile {}", imageId, reptileId);
        Long userId = currentUserId();

        return reptileRepository.findByIdAndUserId(reptileId, userId)
                .flatMap(reptile -> {
                    Optional<ReptileImage> image = reptileImageRepository.findById(imageId);

                    if (image.isEmpty() || !image.get().getReptileId().equals(reptileId)) {
                        log.warn("Image {} does not belong to reptile {}", imageId, reptileId);
                        return Optional.empty();
                    }

                    reptile.setHighlightImageId(imageId);
                    Reptile savedReptile = reptileRepository.save(reptile);
                    log.info("Set highlight image {} for reptile {}", imageId, reptileId);
                    return Optional.of(reptileMapper.toDto(savedReptile));
                });
    }

    /**
     * Removes the highlight image from a reptile, verifying ownership.
     * @param reptileId the reptile ID
     * @return true if removed successfully
     */
    public boolean removeHighlightImage(Long reptileId) {
        log.info("Removing highlight image for reptile {}", reptileId);
        Long userId = currentUserId();

        return reptileRepository.findByIdAndUserId(reptileId, userId)
                .map(reptile -> {
                    reptile.setHighlightImageId(null);
                    reptileRepository.save(reptile);
                    log.info("Removed highlight image for reptile {}", reptileId);
                    return true;
                })
                .orElse(false);
    }

    /**
     * Gets statistics about reptiles for the current user.
     * @return statistics object with counts
     */
    @Transactional(readOnly = true)
    public ReptileStatistics getStatistics() {
        log.debug("Retrieving reptile statistics for current user");
        Long userId = currentUserId();

        long totalCount = reptileRepository.countByUserId(userId);
        long activeCount = reptileRepository.countByUserIdAndStatus(userId, Reptile.ReptileStatus.ACTIVE);
        long quarantineCount = reptileRepository.countByUserIdAndStatus(userId, Reptile.ReptileStatus.QUARANTINE);
        long deceasedCount = reptileRepository.countByUserIdAndStatus(userId, Reptile.ReptileStatus.DECEASED);

        return new ReptileStatistics(totalCount, activeCount, quarantineCount, deceasedCount);
    }

    /**
     * Inner class for reptile statistics.
     */
    public static class ReptileStatistics {
        private final long total;
        private final long active;
        private final long quarantine;
        private final long deceased;

        public ReptileStatistics(long total, long active, long quarantine, long deceased) {
            this.total = total;
            this.active = active;
            this.quarantine = quarantine;
            this.deceased = deceased;
        }

        public long getTotal() { return total; }
        public long getActive() { return active; }
        public long getQuarantine() { return quarantine; }
        public long getDeceased() { return deceased; }
    }
}
package com.reptilemanagement.rest.service;

import com.reptilemanagement.persistence.domain.Reptile;
import com.reptilemanagement.persistence.domain.ReptileImage;
import com.reptilemanagement.persistence.dto.ReptileDto;
import com.reptilemanagement.persistence.mapper.ReptileMapper;
import com.reptilemanagement.persistence.repository.ReptileImageRepository;
import com.reptilemanagement.persistence.repository.ReptileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for managing reptile operations.
 * Provides business logic for CRUD operations on reptiles.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ReptileService {

    private final ReptileRepository reptileRepository;
    private final ReptileImageRepository reptileImageRepository;
    private final ReptileMapper reptileMapper;

    /**
     * Creates a new reptile.
     * @param reptileDto the reptile data to create
     * @return the created reptile as DTO
     */
    public ReptileDto createReptile(ReptileDto reptileDto) {
        log.info("Creating new reptile: {}", reptileDto.getName());

        Reptile reptile = reptileMapper.toEntity(reptileDto);
        Reptile savedReptile = reptileRepository.save(reptile);

        log.info("Created reptile with ID: {}", savedReptile.getId());
        return reptileMapper.toDto(savedReptile);
    }

    /**
     * Retrieves a reptile by ID.
     * @param id the reptile ID
     * @return the reptile as DTO, or empty if not found
     */
    @Transactional(readOnly = true)
    public Optional<ReptileDto> getReptileById(Long id) {
        log.debug("Retrieving reptile with ID: {}", id);

        return reptileRepository.findById(id)
                .map(reptileMapper::toDto);
    }

    /**
     * Retrieves all reptiles.
     * @return list of all reptiles as DTOs
     */
    @Transactional(readOnly = true)
    public List<ReptileDto> getAllReptiles() {
        log.debug("Retrieving all reptiles");

        return reptileRepository.findAll().stream()
                .map(reptileMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all active reptiles.
     * @return list of active reptiles as DTOs
     */
    @Transactional(readOnly = true)
    public List<ReptileDto> getActiveReptiles() {
        log.debug("Retrieving active reptiles");

        return reptileRepository.findByStatus(Reptile.ReptileStatus.ACTIVE).stream()
                .map(reptileMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves reptiles by species.
     * @param species the species to search for
     * @return list of reptiles of the specified species
     */
    @Transactional(readOnly = true)
    public List<ReptileDto> getReptilesBySpecies(String species) {
        log.debug("Retrieving reptiles by species: {}", species);

        return reptileRepository.findBySpeciesContainingIgnoreCase(species).stream()
                .map(reptileMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves reptiles by name.
     * @param name the name to search for
     * @return list of reptiles with names containing the search term
     */
    @Transactional(readOnly = true)
    public List<ReptileDto> getReptilesByName(String name) {
        log.debug("Retrieving reptiles by name: {}", name);

        return reptileRepository.findByNameContainingIgnoreCase(name).stream()
                .map(reptileMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves reptiles in a specific enclosure.
     * @param enclosureId the enclosure ID
     * @return list of reptiles in the specified enclosure
     */
    @Transactional(readOnly = true)
    public List<ReptileDto> getReptilesByEnclosure(Long enclosureId) {
        log.debug("Retrieving reptiles in enclosure: {}", enclosureId);

        return reptileRepository.findByEnclosureId(enclosureId).stream()
                .map(reptileMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Updates an existing reptile.
     * @param id the reptile ID to update
     * @param reptileDto the updated reptile data
     * @return the updated reptile as DTO, or empty if not found
     */
    public Optional<ReptileDto> updateReptile(Long id, ReptileDto reptileDto) {
        log.info("Updating reptile with ID: {}", id);

        return reptileRepository.findById(id)
                .map(existingReptile -> {
                    Reptile updatedReptile = reptileMapper.toEntity(reptileDto);
                    updatedReptile.setId(id);
                    updatedReptile.setCreatedAt(existingReptile.getCreatedAt());
                    Reptile savedReptile = reptileRepository.save(updatedReptile);

                    log.info("Updated reptile with ID: {}", id);
                    return reptileMapper.toDto(savedReptile);
                });
    }

    /**
     * Deletes a reptile by ID.
     * @param id the reptile ID to delete
     * @return true if deleted, false if not found
     */
    public boolean deleteReptile(Long id) {
        log.info("Deleting reptile with ID: {}", id);

        if (reptileRepository.existsById(id)) {
            reptileRepository.deleteById(id);
            log.info("Deleted reptile with ID: {}", id);
            return true;
        }

        log.warn("Reptile with ID {} not found for deletion", id);
        return false;
    }

    /**
     * Moves a reptile to a different enclosure.
     * @param reptileId the reptile ID
     * @param enclosureId the new enclosure ID (null to remove from enclosure)
     * @return true if moved successfully, false if reptile not found
     */
    public boolean moveReptileToEnclosure(Long reptileId, Long enclosureId) {
        log.info("Moving reptile {} to enclosure {}", reptileId, enclosureId);

        return reptileRepository.findById(reptileId)
                .map(reptile -> {
                    reptile.setEnclosureId(enclosureId);
                    reptileRepository.save(reptile);
                    log.info("Moved reptile {} to enclosure {}", reptileId, enclosureId);
                    return true;
                })
                .orElse(false);
    }

    /**
     * Updates the status of a reptile.
     * @param reptileId the reptile ID
     * @param status the new status
     * @return true if updated successfully, false if reptile not found
     */
    public boolean updateReptileStatus(Long reptileId, Reptile.ReptileStatus status) {
        log.info("Updating reptile {} status to {}", reptileId, status);

        return reptileRepository.findById(reptileId)
                .map(reptile -> {
                    reptile.setStatus(status);
                    reptileRepository.save(reptile);
                    log.info("Updated reptile {} status to {}", reptileId, status);
                    return true;
                })
                .orElse(false);
    }

    /**
     * Sets the highlight image for a reptile.
     * Validates that the image belongs to the reptile before setting.
     * @param reptileId the reptile ID
     * @param imageId the image ID to set as highlight
     * @return the updated reptile as DTO, or empty if reptile not found or image doesn't belong to reptile
     */
    public Optional<ReptileDto> setHighlightImage(Long reptileId, Long imageId) {
        log.info("Setting highlight image {} for reptile {}", imageId, reptileId);

        return reptileRepository.findById(reptileId)
                .flatMap(reptile -> {
                    // Validate that the image belongs to this reptile
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
     * Removes the highlight image from a reptile.
     * @param reptileId the reptile ID
     * @return true if removed successfully, false if reptile not found
     */
    public boolean removeHighlightImage(Long reptileId) {
        log.info("Removing highlight image for reptile {}", reptileId);

        return reptileRepository.findById(reptileId)
                .map(reptile -> {
                    reptile.setHighlightImageId(null);
                    reptileRepository.save(reptile);
                    log.info("Removed highlight image for reptile {}", reptileId);
                    return true;
                })
                .orElse(false);
    }

    /**
     * Gets statistics about reptiles.
     * @return statistics object with counts
     */
    @Transactional(readOnly = true)
    public ReptileStatistics getStatistics() {
        log.debug("Retrieving reptile statistics");

        long totalCount = reptileRepository.count();
        long activeCount = reptileRepository.countByStatus(Reptile.ReptileStatus.ACTIVE);
        long quarantineCount = reptileRepository.countByStatus(Reptile.ReptileStatus.QUARANTINE);
        long deceasedCount = reptileRepository.countByStatus(Reptile.ReptileStatus.DECEASED);

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
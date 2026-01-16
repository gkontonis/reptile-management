package com.reptilemanagement.rest.controller;

import com.reptilemanagement.domain.Reptile;
import com.reptilemanagement.dto.ReptileDto;
import com.reptilemanagement.dto.ReptileImageDto;
import com.reptilemanagement.rest.service.ReptileImageService;
import com.reptilemanagement.rest.service.ReptileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * REST controller for managing reptile operations.
 * Provides endpoints for CRUD operations on reptiles.
 */
@RestController
@RequestMapping("/api/reptiles")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('USER')")
public class ReptileController {

    private final ReptileService reptileService;
    private final ReptileImageService reptileImageService;

    /**
     * Creates a new reptile.
     * 
     * @param reptileDto the reptile data
     * @return the created reptile
     */
    @PostMapping
    public ResponseEntity<ReptileDto> createReptile(@Valid @RequestBody ReptileDto reptileDto) {
        log.info("REST request to create reptile: {}", reptileDto.getName());

        ReptileDto result = reptileService.createReptile(reptileDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    /**
     * Retrieves all reptiles.
     * 
     * @return list of all reptiles
     */
    @GetMapping
    public ResponseEntity<List<ReptileDto>> getAllReptiles() {
        log.debug("REST request to get all reptiles");

        List<ReptileDto> reptiles = reptileService.getAllReptiles();
        return ResponseEntity.ok(reptiles);
    }

    /**
     * Retrieves all active reptiles.
     * 
     * @return list of active reptiles
     */
    @GetMapping("/active")
    public ResponseEntity<List<ReptileDto>> getActiveReptiles() {
        log.debug("REST request to get active reptiles");

        List<ReptileDto> reptiles = reptileService.getActiveReptiles();
        return ResponseEntity.ok(reptiles);
    }

    /**
     * Retrieves a reptile by ID.
     * 
     * @param id the reptile ID
     * @return the reptile
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReptileDto> getReptile(@PathVariable Long id) {
        log.debug("REST request to get reptile: {}", id);

        return reptileService.getReptileById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Searches reptiles by species.
     * 
     * @param species the species to search for
     * @return list of reptiles matching the species
     */
    @GetMapping("/search/species")
    public ResponseEntity<List<ReptileDto>> searchBySpecies(@RequestParam String species) {
        log.debug("REST request to search reptiles by species: {}", species);

        List<ReptileDto> reptiles = reptileService.getReptilesBySpecies(species);
        return ResponseEntity.ok(reptiles);
    }

    /**
     * Searches reptiles by name.
     * 
     * @param name the name to search for
     * @return list of reptiles matching the name
     */
    @GetMapping("/search/name")
    public ResponseEntity<List<ReptileDto>> searchByName(@RequestParam String name) {
        log.debug("REST request to search reptiles by name: {}", name);

        List<ReptileDto> reptiles = reptileService.getReptilesByName(name);
        return ResponseEntity.ok(reptiles);
    }

    /**
     * Retrieves reptiles in a specific enclosure.
     * 
     * @param enclosureId the enclosure ID
     * @return list of reptiles in the enclosure
     */
    @GetMapping("/enclosure/{enclosureId}")
    public ResponseEntity<List<ReptileDto>> getReptilesByEnclosure(@PathVariable Long enclosureId) {
        log.debug("REST request to get reptiles in enclosure: {}", enclosureId);

        List<ReptileDto> reptiles = reptileService.getReptilesByEnclosure(enclosureId);
        return ResponseEntity.ok(reptiles);
    }

    /**
     * Updates an existing reptile.
     * 
     * @param id         the reptile ID
     * @param reptileDto the updated reptile data
     * @return the updated reptile
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReptileDto> updateReptile(@PathVariable Long id, @Valid @RequestBody ReptileDto reptileDto) {
        log.info("REST request to update reptile: {}", id);

        return reptileService.updateReptile(id, reptileDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Moves a reptile to a different enclosure.
     * 
     * @param id          the reptile ID
     * @param enclosureId the new enclosure ID (null to remove from enclosure)
     * @return success status
     */
    @PatchMapping("/{id}/enclosure")
    public ResponseEntity<Void> moveReptileToEnclosure(@PathVariable Long id,
            @RequestParam(required = false) Long enclosureId) {
        log.info("REST request to move reptile {} to enclosure {}", id, enclosureId);

        boolean success = reptileService.moveReptileToEnclosure(id, enclosureId);
        return success ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    /**
     * Updates the status of a reptile.
     * 
     * @param id     the reptile ID
     * @param status the new status
     * @return success status
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateReptileStatus(@PathVariable Long id, @RequestParam Reptile.ReptileStatus status) {
        log.info("REST request to update reptile {} status to {}", id, status);

        boolean success = reptileService.updateReptileStatus(id, status);
        return success ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    /**
     * Deletes a reptile.
     * 
     * @param id the reptile ID
     * @return success status
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReptile(@PathVariable Long id) {
        log.info("REST request to delete reptile: {}", id);

        boolean success = reptileService.deleteReptile(id);
        return success ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    /**
     * Gets reptile statistics.
     * 
     * @return statistics data
     */
    @GetMapping("/statistics")
    public ResponseEntity<ReptileService.ReptileStatistics> getStatistics() {
        log.debug("REST request to get reptile statistics");

        ReptileService.ReptileStatistics statistics = reptileService.getStatistics();
        return ResponseEntity.ok(statistics);
    }

    // ==================== Image Endpoints ====================

    /**
     * Uploads an image for a reptile.
     * 
     * @param id          the reptile ID
     * @param file        the image file (multipart/form-data)
     * @param description optional description for the image
     * @return the created image metadata
     */
    @PostMapping(value = "/{id}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ReptileImageDto> uploadImage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "description", required = false) String description) throws IOException {
        log.info("REST request to upload image for reptile: {}", id);

        ReptileImageDto result = reptileImageService.uploadImage(id, file, description);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    /**
     * Retrieves all image metadata for a reptile.
     * 
     * @param id the reptile ID
     * @return list of image metadata
     */
    @GetMapping("/{id}/images")
    public ResponseEntity<List<ReptileImageDto>> getImages(@PathVariable Long id) {
        log.debug("REST request to get images for reptile: {}", id);

        List<ReptileImageDto> images = reptileImageService.getImagesByReptileId(id);
        return ResponseEntity.ok(images);
    }

    /**
     * Retrieves an image binary data by ID.
     * 
     * @param id      the reptile ID
     * @param imageId the image ID
     * @return the image binary data with appropriate content type
     */
    @GetMapping("/{id}/images/{imageId}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id, @PathVariable Long imageId) {
        log.debug("REST request to get image {} for reptile: {}", imageId, id);

        return reptileImageService.getImageById(imageId)
                .filter(image -> image.getReptileId().equals(id))
                .map(image -> ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + image.getFilename() + "\"")
                        .contentType(MediaType.parseMediaType(image.getContentType()))
                        .contentLength(image.getSize())
                        .body(image.getImageData()))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Deletes an image by ID.
     * 
     * @param id      the reptile ID
     * @param imageId the image ID
     * @return success status
     */
    @DeleteMapping("/{id}/images/{imageId}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long id, @PathVariable Long imageId) {
        log.info("REST request to delete image {} for reptile: {}", imageId, id);

        // First verify the image belongs to this reptile
        boolean belongs = reptileImageService.getImageMetadataById(imageId)
                .map(img -> img.getReptileId().equals(id))
                .orElse(false);

        if (!belongs) {
            return ResponseEntity.notFound().build();
        }

        boolean success = reptileImageService.deleteImage(imageId);
        return success ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    /**
     * Sets or updates the highlight image for a reptile.
     *
     * @param id      the reptile ID
     * @param imageId the image ID to set as highlight (must belong to this reptile)
     * @return the updated reptile DTO
     */
    @PatchMapping("/{id}/highlight-image")
    public ResponseEntity<ReptileDto> setHighlightImage(
            @PathVariable Long id,
            @RequestParam Long imageId) {
        log.info("REST request to set highlight image {} for reptile {}", imageId, id);

        return reptileService.setHighlightImage(id, imageId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Removes the highlight image from a reptile.
     *
     * @param id the reptile ID
     * @return success status
     */
    @DeleteMapping("/{id}/highlight-image")
    public ResponseEntity<Void> removeHighlightImage(@PathVariable Long id) {
        log.info("REST request to remove highlight image for reptile {}", id);

        boolean success = reptileService.removeHighlightImage(id);
        return success ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
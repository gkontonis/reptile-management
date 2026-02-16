package com.reptilemanagement.rest.service;

import com.reptilemanagement.persistence.domain.ReptileImage;
import com.reptilemanagement.persistence.dto.ReptileImageDto;
import com.reptilemanagement.persistence.mapper.ReptileImageMapper;
import com.reptilemanagement.persistence.mapper.base.BaseMapper;
import com.reptilemanagement.persistence.repository.ReptileImageRepository;
import com.reptilemanagement.persistence.repository.ReptileRepository;
import com.reptilemanagement.rest.service.base.BaseCrudService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service class for managing reptile image operations.
 * Provides business logic for uploading, retrieving, and deleting reptile
 * images.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ReptileImageService extends BaseCrudService<Long, ReptileImage, ReptileImageDto> {

    private final ReptileImageRepository reptileImageRepository;
    private final ReptileRepository reptileRepository;
    private final ReptileImageMapper reptileImageMapper;

    @Override
    protected JpaRepository<ReptileImage, Long> getRepository() {
        return reptileImageRepository;
    }

    @Override
    protected BaseMapper<ReptileImage, ReptileImageDto> getMapper() {
        return reptileImageMapper;
    }

    @Override
    public Sort getDefaultSort() {
        return Sort.by(Sort.Direction.DESC, "createdAt");
    }

    /** Supported image content types */
    private static final Set<String> SUPPORTED_CONTENT_TYPES = Set.of(
            "image/jpeg",
            "image/png",
            "image/gif",
            "image/webp",
            "image/bmp");

    /** Maximum file size in bytes (10 MB) */
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;

    /**
     * Uploads an image for a reptile.
     * 
     * @param reptileId   the reptile ID
     * @param file        the image file to upload
     * @param description optional description for the image
     * @return the created image metadata as DTO
     * @throws IllegalArgumentException if file type is not supported or reptile
     *                                  doesn't exist
     * @throws IOException              if file reading fails
     */
    public ReptileImageDto uploadImage(Long reptileId, MultipartFile file, String description) throws IOException {
        log.info("Uploading image for reptile: {}, filename: {}", reptileId, file.getOriginalFilename());

        // Validate reptile exists
        if (!reptileRepository.existsById(reptileId)) {
            throw new IllegalArgumentException("Reptile not found with ID: " + reptileId);
        }

        // Validate file
        validateFile(file);

        // Create and save the image entity
        ReptileImage image = new ReptileImage();
        image.setReptileId(reptileId);
        image.setFilename(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setImageData(file.getBytes());
        image.setDescription(description);
        image.setSize(file.getSize());

        ReptileImage savedImage = reptileImageRepository.save(image);
        log.info("Uploaded image with ID: {} for reptile: {}", savedImage.getId(), reptileId);

        // Auto-set as highlight if this is the only image for the reptile
        if (reptileImageRepository.countByReptileId(reptileId) == 1) {
            reptileRepository.findById(reptileId).ifPresent(reptile -> {
                reptile.setHighlightImageId(savedImage.getId());
                reptileRepository.save(reptile);
                log.info("Auto-set highlight image to {} for reptile {}", savedImage.getId(), reptileId);
            });
        }

        return reptileImageMapper.toDto(savedImage);
    }

    /**
     * Validates the uploaded file.
     * 
     * @param file the file to validate
     * @throws IllegalArgumentException if validation fails
     */
    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("File size exceeds maximum limit of 10 MB");
        }

        String contentType = file.getContentType();
        if (contentType == null || !SUPPORTED_CONTENT_TYPES.contains(contentType.toLowerCase())) {
            throw new IllegalArgumentException(
                    "Unsupported file type: " + contentType +
                            ". Supported types: JPEG, PNG, GIF, WebP, BMP");
        }
    }

    /**
     * Retrieves an image by ID with binary data.
     * 
     * @param imageId the image ID
     * @return the image entity with binary data, or empty if not found
     */
    @Transactional(readOnly = true)
    public Optional<ReptileImage> getImageById(Long imageId) {
        log.debug("Retrieving image with ID: {}", imageId);
        return reptileImageRepository.findById(imageId);
    }

    /**
     * Retrieves image metadata by ID (without binary data).
     * 
     * @param imageId the image ID
     * @return the image metadata as DTO, or empty if not found
     */
    @Transactional(readOnly = true)
    public Optional<ReptileImageDto> getImageMetadataById(Long imageId) {
        log.debug("Retrieving image metadata with ID: {}", imageId);
        return reptileImageRepository.findById(imageId)
                .map(reptileImageMapper::toDto);
    }

    /**
     * Retrieves all image metadata for a specific reptile.
     * 
     * @param reptileId the reptile ID
     * @return list of image metadata DTOs
     */
    @Transactional(readOnly = true)
    public List<ReptileImageDto> getImagesByReptileId(Long reptileId) {
        log.debug("Retrieving images for reptile: {}", reptileId);
        return reptileImageRepository.findByReptileIdOrderByUpdatedAtDesc(reptileId).stream()
                .map(reptileImageMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Deletes an image by ID.
     * 
     * @param imageId the image ID
     * @return true if deleted, false if not found
     */
    public boolean deleteImage(Long imageId) {
        log.info("Deleting image with ID: {}", imageId);

        if (reptileImageRepository.existsById(imageId)) {
            // Clean up any reptiles that have this as their highlight image
            Optional<ReptileImage> image = reptileImageRepository.findById(imageId);
            if (image.isPresent()) {
                Long reptileId = image.get().getReptileId();
                reptileRepository.findById(reptileId).ifPresent(reptile -> {
                    if (imageId.equals(reptile.getHighlightImageId())) {
                        reptile.setHighlightImageId(null);
                        reptileRepository.save(reptile);
                        log.info("Cleared highlight image reference for reptile {}", reptileId);
                    }
                });
            }

            reptileImageRepository.deleteById(imageId);
            log.info("Deleted image with ID: {}", imageId);
            return true;
        }

        log.warn("Image with ID {} not found for deletion", imageId);
        return false;
    }

    /**
     * Deletes all images for a specific reptile.
     * 
     * @param reptileId the reptile ID
     * @return number of images deleted
     */
    public long deleteImagesByReptileId(Long reptileId) {
        log.info("Deleting all images for reptile: {}", reptileId);

        long count = reptileImageRepository.countByReptileId(reptileId);
        reptileImageRepository.deleteByReptileId(reptileId);

        log.info("Deleted {} images for reptile: {}", count, reptileId);
        return count;
    }

    /**
     * Counts images for a specific reptile.
     * 
     * @param reptileId the reptile ID
     * @return number of images
     */
    @Transactional(readOnly = true)
    public long countImagesByReptileId(Long reptileId) {
        return reptileImageRepository.countByReptileId(reptileId);
    }
}

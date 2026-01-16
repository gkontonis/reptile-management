package com.reptilemanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for ReptileImage entity.
 * Excludes the binary image data for lightweight responses.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReptileImageDto {
    /** Unique identifier for the image */
    private Long id;

    /** ID of the reptile this image belongs to */
    private Long reptileId;

    /** Original filename of the uploaded image */
    private String filename;

    /** MIME type of the image */
    private String contentType;

    /** Optional description of the image */
    private String description;

    /** Size of the image in bytes */
    private Long size;

    /** Timestamp when the image was uploaded */
    private LocalDateTime uploadedAt;
}

package com.reptilemanagement.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entity representing an image of a reptile.
 * Images are stored as BLOBs in the database.
 */
@Entity
@Table(name = "reptile_images")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReptileImage {
    /** Unique identifier for the image */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** ID of the reptile this image belongs to */
    @Column(nullable = false)
    private Long reptileId;

    /** Original filename of the uploaded image */
    @Column(nullable = false)
    private String filename;

    /** MIME type of the image (e.g., image/jpeg, image/png) */
    @Column(nullable = false)
    private String contentType;

    /** Binary data of the image stored as a byte array */
    @Basic(fetch = FetchType.LAZY)
    @Column(nullable = false)
    private byte[] imageData;

    /** Optional description of the image */
    @Column(length = 500)
    private String description;

    /** Size of the image in bytes */
    @Column(nullable = false)
    private Long size;

    /** Timestamp when the image was uploaded */
    private LocalDateTime uploadedAt;

    /**
     * JPA lifecycle callback executed before persisting the entity.
     * Sets the upload timestamp to current time.
     */
    @PrePersist
    protected void onCreate() {
        uploadedAt = LocalDateTime.now();
    }
}

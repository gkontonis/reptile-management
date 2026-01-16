package com.reptilemanagement.persistence.domain;

import com.reptilemanagement.persistence.domain.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Entity representing an image of a reptile.
 * Images are stored as BLOBs in the database.
 */
@Entity
@Table(name = "reptile_images")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ReptileImage extends BaseEntity<Long> {
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
}

-- ============================================================
-- V1: Initial schema setup
-- ============================================================

-- users
CREATE TABLE users (
    id          BIGSERIAL PRIMARY KEY,
    username    VARCHAR(255) NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    email       VARCHAR(255) NOT NULL,
    created_by  VARCHAR(255),
    updated_by  VARCHAR(255),
    created_at  TIMESTAMP    NOT NULL,
    updated_at  TIMESTAMP
);

-- user_roles (collection table for User.roles Set<String>)
CREATE TABLE user_roles (
    user_id BIGINT       NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    role    VARCHAR(255) NOT NULL,
    PRIMARY KEY (user_id, role)
);

-- enclosures
CREATE TABLE enclosures (
    id          BIGSERIAL    PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    type        VARCHAR(50)  NOT NULL,
    dimensions  VARCHAR(255),
    substrate   VARCHAR(255),
    heating     VARCHAR(255),
    lighting    VARCHAR(255),
    humidity    VARCHAR(255),
    temperature VARCHAR(255),
    user_id     BIGINT       REFERENCES users (id),
    notes       VARCHAR(1000),
    created_by  VARCHAR(255) NOT NULL,
    updated_by  VARCHAR(255),
    created_at  TIMESTAMP    NOT NULL,
    updated_at  TIMESTAMP
);

-- reptiles
CREATE TABLE reptiles (
    id                BIGSERIAL    PRIMARY KEY,
    name              VARCHAR(255) NOT NULL,
    species           VARCHAR(255) NOT NULL,
    subspecies        VARCHAR(255),
    gender            VARCHAR(20)  NOT NULL,
    birth_date        DATE,
    acquisition_date  DATE         NOT NULL,
    enclosure_id      BIGINT       REFERENCES enclosures (id),
    status            VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE',
    user_id           BIGINT       REFERENCES users (id),
    notes             VARCHAR(1000),
    highlight_image_id BIGINT,
    created_by        VARCHAR(255) NOT NULL,
    updated_by        VARCHAR(255),
    created_at        TIMESTAMP    NOT NULL,
    updated_at        TIMESTAMP
);

-- reptile_images
CREATE TABLE reptile_images (
    id           BIGSERIAL    PRIMARY KEY,
    reptile_id   BIGINT       NOT NULL REFERENCES reptiles (id) ON DELETE CASCADE,
    filename     VARCHAR(255) NOT NULL,
    content_type VARCHAR(255) NOT NULL,
    image_data   BYTEA        NOT NULL,
    description  VARCHAR(500),
    size         BIGINT       NOT NULL,
    created_by   VARCHAR(255) NOT NULL,
    updated_by   VARCHAR(255),
    created_at   TIMESTAMP    NOT NULL,
    updated_at   TIMESTAMP
);

-- Add FK from reptiles.highlight_image_id → reptile_images.id (deferred to avoid circular dependency)
ALTER TABLE reptiles
    ADD CONSTRAINT fk_reptile_highlight_image
    FOREIGN KEY (highlight_image_id) REFERENCES reptile_images (id);

-- feeding_logs
CREATE TABLE feeding_logs (
    id           BIGSERIAL    PRIMARY KEY,
    reptile_id   BIGINT       NOT NULL REFERENCES reptiles (id) ON DELETE CASCADE,
    feeding_date TIMESTAMP    NOT NULL,
    food_type    VARCHAR(255) NOT NULL,
    quantity     VARCHAR(255) NOT NULL,
    ate          BOOLEAN      NOT NULL DEFAULT TRUE,
    notes        VARCHAR(500),
    created_by   VARCHAR(255) NOT NULL,
    updated_by   VARCHAR(255),
    created_at   TIMESTAMP    NOT NULL,
    updated_at   TIMESTAMP
);

-- weight_logs
CREATE TABLE weight_logs (
    id               BIGSERIAL      PRIMARY KEY,
    reptile_id       BIGINT         NOT NULL REFERENCES reptiles (id) ON DELETE CASCADE,
    measurement_date TIMESTAMP      NOT NULL,
    weight_grams     NUMERIC(8, 2)  NOT NULL,
    notes            VARCHAR(500),
    created_by       VARCHAR(255)   NOT NULL,
    updated_by       VARCHAR(255),
    created_at       TIMESTAMP      NOT NULL,
    updated_at       TIMESTAMP
);

-- shedding_logs
CREATE TABLE shedding_logs (
    id            BIGSERIAL    PRIMARY KEY,
    reptile_id    BIGINT       NOT NULL REFERENCES reptiles (id) ON DELETE CASCADE,
    shedding_date TIMESTAMP    NOT NULL,
    shed_quality  VARCHAR(255) NOT NULL,
    ate_shed      BOOLEAN,
    notes         VARCHAR(500),
    created_by    VARCHAR(255) NOT NULL,
    updated_by    VARCHAR(255),
    created_at    TIMESTAMP    NOT NULL,
    updated_at    TIMESTAMP
);

-- poop_logs
CREATE TABLE poop_logs (
    id                BIGSERIAL    PRIMARY KEY,
    reptile_id        BIGINT       NOT NULL REFERENCES reptiles (id) ON DELETE CASCADE,
    poop_date         TIMESTAMP    NOT NULL,
    consistency       VARCHAR(20)  NOT NULL,
    color             VARCHAR(100),
    parasites_present BOOLEAN      NOT NULL DEFAULT FALSE,
    notes             VARCHAR(500),
    created_by        VARCHAR(255) NOT NULL,
    updated_by        VARCHAR(255),
    created_at        TIMESTAMP    NOT NULL,
    updated_at        TIMESTAMP
);

-- enclosure_cleanings
CREATE TABLE enclosure_cleanings (
    id                BIGSERIAL   PRIMARY KEY,
    enclosure_id      BIGINT      NOT NULL REFERENCES enclosures (id) ON DELETE CASCADE,
    cleaning_date     TIMESTAMP   NOT NULL,
    cleaning_type     VARCHAR(20) NOT NULL,
    substrate_changed BOOLEAN     NOT NULL DEFAULT FALSE,
    disinfected       BOOLEAN     NOT NULL DEFAULT FALSE,
    notes             VARCHAR(500),
    created_by        VARCHAR(255) NOT NULL,
    updated_by        VARCHAR(255),
    created_at        TIMESTAMP    NOT NULL,
    updated_at        TIMESTAMP
);

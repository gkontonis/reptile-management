-- ============================================================
-- V2: Seed data — default admin user and test reptiles
-- ============================================================

-- Default admin user
-- Password: Admin123!  (BCrypt strength 10)
INSERT INTO users (username, password, email, created_by, updated_by, created_at, updated_at)
VALUES (
    'admin',
    '$2a$10$HVjT7ONvwUHPm8U5Fyga9.56IaFr9xKng9MUw6aQ55gM8/JD.PkCK',
    'admin@homemanagement.local',
    'system',
    'system',
    NOW(),
    NOW()
);

INSERT INTO user_roles (user_id, role)
SELECT id, 'ROLE_ADMIN' FROM users WHERE username = 'admin'
UNION ALL
SELECT id, 'ROLE_USER'  FROM users WHERE username = 'admin';

-- Test reptile 1: Butters — Ball Python
INSERT INTO reptiles (name, species, subspecies, gender, birth_date, acquisition_date, status, user_id, notes, created_by, updated_by, created_at, updated_at)
SELECT
    'Butters',
    'Ball Python',
    'Banana Spider Morph',
    'MALE',
    '2020-01-01',
    '2023-05-03',
    'ACTIVE',
    id,
    'Beautiful banana spider morph with stunning yellow coloration.',
    'system',
    'system',
    NOW(),
    NOW()
FROM users WHERE username = 'admin';

-- Test reptile 2: Spike — Bearded Dragon
INSERT INTO reptiles (name, species, subspecies, gender, birth_date, acquisition_date, status, user_id, notes, created_by, updated_by, created_at, updated_at)
SELECT
    'Spike',
    'Bearded Dragon',
    'Central Bearded Dragon',
    'MALE',
    '2021-08-10',
    '2021-10-15',
    'ACTIVE',
    id,
    'Loves crickets and greens. Enjoys basking under heat lamp.',
    'system',
    'system',
    NOW(),
    NOW()
FROM users WHERE username = 'admin';

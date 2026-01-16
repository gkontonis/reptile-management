package com.reptilemanagement.security;

import com.reptilemanagement.persistence.domain.Reptile;
import com.reptilemanagement.persistence.domain.User;
import com.reptilemanagement.persistence.repository.ReptileRepository;
import com.reptilemanagement.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Set;

/**
 * Initializes the database with required data on application startup.
 * Creates the initial admin user if no admin exists.
 * Optionally creates test reptile data for UI testing.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ReptileRepository reptileRepository;

    @Override
    public void run(String... args) throws Exception {
        createAdminUserIfNotExists();
        createTestReptilesIfEmpty();
    }

    private void createAdminUserIfNotExists() {
        // Check if any user with ROLE_ADMIN exists
        boolean adminExists = userRepository.findAll().stream()
                .anyMatch(user -> user.getRoles().contains(RoleConstants.ADMIN));

        if (!adminExists) {
            log.info("No admin user found. Creating default admin user...");

            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@homemanagement.local");
            // Default password: Admin123! (MUST be changed after first login)
            admin.setPassword(passwordEncoder.encode("Admin123!"));
            admin.setRoles(Set.of(RoleConstants.ADMIN, RoleConstants.USER));

            userRepository.save(admin);

            log.info("======================================");
            log.info("ADMIN USER CREATED SUCCESSFULLY");
            log.info("Username: admin");
            log.info("Password: Admin123!");
            log.info("IMPORTANT: Please change this password immediately after first login!");
            log.info("======================================");
        } else {
            log.debug("Admin user already exists. Skipping initialization.");
        }
    }

    /**
     * Creates test reptile data if the database is empty.
     * This provides sample data for UI testing and development.
     */
    private void createTestReptilesIfEmpty() {
        long reptileCount = reptileRepository.count();
        
        if (reptileCount == 0) {
            log.info("No reptiles found. Creating test reptile data for UI testing...");
            
            // Test Reptile 1: Butters - Ball Python
            Reptile butters = new Reptile();
            butters.setName("Butters");
            butters.setSpecies("Ball Python");
            butters.setSubspecies("Banana Spider Morph");
            butters.setGender(Reptile.ReptileGender.MALE);
            butters.setBirthDate(LocalDate.of(2020, 1, 1));
            butters.setAcquisitionDate(LocalDate.of(2023, 5, 3));
            butters.setStatus(Reptile.ReptileStatus.ACTIVE);
            butters.setNotes("Beautiful banana spider morph with stunning yellow coloration.");
            reptileRepository.save(butters);
            
            // Test Reptile 2: Bearded Dragon
            Reptile beardedDragon = new Reptile();
            beardedDragon.setName("Spike");
            beardedDragon.setSpecies("Bearded Dragon");
            beardedDragon.setSubspecies("Central Bearded Dragon");
            beardedDragon.setGender(Reptile.ReptileGender.MALE);
            beardedDragon.setBirthDate(LocalDate.of(2021, 8, 10));
            beardedDragon.setAcquisitionDate(LocalDate.of(2021, 10, 15));
            beardedDragon.setStatus(Reptile.ReptileStatus.ACTIVE);
            beardedDragon.setNotes("Loves crickets and greens. Enjoys basking under heat lamp.");
            reptileRepository.save(beardedDragon);
            
            log.info("======================================");
            log.info("TEST REPTILE DATA CREATED");
            log.info("Created 2 test reptiles for UI testing:");
            log.info("  1. Butters - Ball Python (Banana Spider Morph)");
            log.info("  2. Spike - Bearded Dragon");
            log.info("======================================");
        } else {
            log.debug("Reptiles already exist in database. Skipping test data creation.");
        }
    }
}

package com.reptilemanagement.shared.auth;

import com.reptilemanagement.persistence.repository.UserRepository;
import com.reptilemanagement.security.AuthenticationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Provider for authentication information.
 * Wraps the static authentication utilities for dependency injection in services.
 */
@Component
@RequiredArgsConstructor
public class AuthenticationInformationProvider {

    private final UserRepository userRepository;

    /**
     * Gets the identifier of the currently authenticated user.
     * 
     * @return the username of the authenticated user, or "system" if not authenticated
     */
    public String getAuthenticatedIdentifier() {
        return AuthenticationUtils.getAuthenticatedIdentifier();
    }

    /**
     * Gets the database ID of the currently authenticated user.
     * 
     * @return the user ID
     * @throws IllegalStateException if the authenticated user is not found in the database
     */
    public Long getAuthenticatedUserId() {
        String username = getAuthenticatedIdentifier();
        return userRepository.findByUsername(username)
                .map(user -> user.getId())
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found: " + username));
    }
}

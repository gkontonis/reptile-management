package com.reptilemanagement.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Utility class for retrieving authentication information.
 */
public final class AuthenticationUtils {

    private AuthenticationUtils() {
        // Utility class - prevent instantiation
    }

    /**
     * Gets the currently authenticated user's identifier (username).
     * Returns "system" if no authentication context is available.
     *
     * @return the authenticated username or "system"
     */
    public static String getAuthenticatedIdentifier() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return "system";
        }

        Object principal = authentication.getPrincipal();
        
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
        
        if (principal instanceof String) {
            return (String) principal;
        }

        return "system";
    }
}

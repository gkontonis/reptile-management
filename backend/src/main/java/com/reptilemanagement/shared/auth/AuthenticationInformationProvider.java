package com.reptilemanagement.shared.auth;

import com.reptilemanagement.security.AuthenticationUtils;
import org.springframework.stereotype.Component;

/**
 * Provider for authentication information.
 * Wraps the static authentication utilities for dependency injection in services.
 */
@Component
public class AuthenticationInformationProvider {
    
    /**
     * Gets the identifier of the currently authenticated user.
     * 
     * @return the username of the authenticated user, or "system" if not authenticated
     */
    public String getAuthenticatedIdentifier() {
        return AuthenticationUtils.getAuthenticatedIdentifier();
    }
}

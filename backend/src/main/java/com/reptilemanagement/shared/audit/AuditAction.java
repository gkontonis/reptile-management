package com.reptilemanagement.shared.audit;

/**
 * Constants for audit action types.
 */
public class AuditAction {
    public static final String CREATE = ".create";
    public static final String UPDATE = ".update";
    public static final String DELETE = ".delete";
    public static final String ACCESS = ".access";
    
    private AuditAction() {
        // Private constructor to prevent instantiation
    }
}

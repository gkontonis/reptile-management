package com.reptilemanagement.shared.audit;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Singleton for collecting and managing audit resource types and actions.
 * Maps entity classes to their resource type names and base actions.
 */
public class AuditResourceCollector {
    
    private static final AuditResourceCollector INSTANCE = new AuditResourceCollector();
    private final Map<Class<?>, String> resourceTypes = new ConcurrentHashMap<>();
    private final Map<Class<?>, String> baseActions = new ConcurrentHashMap<>();
    
    private AuditResourceCollector() {
        // Private constructor for singleton
    }
    
    public static AuditResourceCollector getInstance() {
        return INSTANCE;
    }
    
    /**
     * Gets the resource type for a given entity class.
     * Defaults to the simple class name if not explicitly registered.
     * 
     * @param clazz the entity class
     * @return the resource type name
     */
    public String getResourceType(Class<?> clazz) {
        return resourceTypes.computeIfAbsent(clazz, c -> c.getSimpleName().toLowerCase());
    }
    
    /**
     * Gets the base action path for a given entity class.
     * Defaults to the simple class name in lowercase if not explicitly registered.
     * 
     * @param clazz the entity class
     * @return the base action path
     */
    public String getBaseActionFromClazz(Class<?> clazz) {
        return baseActions.computeIfAbsent(clazz, c -> c.getSimpleName().toLowerCase());
    }
    
    /**
     * Registers a custom resource type for an entity class.
     * 
     * @param clazz the entity class
     * @param resourceType the resource type name
     */
    public void registerResourceType(Class<?> clazz, String resourceType) {
        resourceTypes.put(clazz, resourceType);
    }
    
    /**
     * Registers a custom base action for an entity class.
     * 
     * @param clazz the entity class
     * @param baseAction the base action path
     */
    public void registerBaseAction(Class<?> clazz, String baseAction) {
        baseActions.put(clazz, baseAction);
    }
}

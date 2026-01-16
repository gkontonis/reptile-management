package com.reptilemanagement.shared.constants;

import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

/**
 * Constants for logging markers.
 * Markers can be used to categorize log messages and configure specific logging behavior.
 */
public class MarkerConstants {
    
    /**
     * Marker for CRUD operation logs.
     */
    public static final Marker CRUD = MarkerFactory.getMarker("CRUD");
    
    /**
     * Marker for security-related logs.
     */
    public static final Marker SECURITY = MarkerFactory.getMarker("SECURITY");
    
    /**
     * Marker for business logic logs.
     */
    public static final Marker BUSINESS = MarkerFactory.getMarker("BUSINESS");
    
    private MarkerConstants() {
        // Private constructor to prevent instantiation
    }
}

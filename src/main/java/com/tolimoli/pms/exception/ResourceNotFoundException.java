package com.tolimoli.pms.exception;

/**
 * Exception thrown when a requested resource is not found
 */
public class ResourceNotFoundException extends PMSException {
    
    public ResourceNotFoundException(String resourceType, Object resourceId) {
        super(String.format("%s not found with ID: %s", resourceType, resourceId), "RESOURCE_NOT_FOUND");
    }
    
    public ResourceNotFoundException(String resourceType, String field, Object value) {
        super(String.format("%s not found with %s: %s", resourceType, field, value), "RESOURCE_NOT_FOUND");
    }
    
    public ResourceNotFoundException(String message) {
        super(message, "RESOURCE_NOT_FOUND");
    }
}
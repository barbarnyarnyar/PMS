package com.tolimoli.pms.exception;

/**
 * Base exception class for PMS application
 */
public class PMSException extends RuntimeException {
    
    private final String errorCode;
    
    public PMSException(String message) {
        super(message);
        this.errorCode = "PMS_ERROR";
    }
    
    public PMSException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public PMSException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "PMS_ERROR";
    }
    
    public PMSException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
}
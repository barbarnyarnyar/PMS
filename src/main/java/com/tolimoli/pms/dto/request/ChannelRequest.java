package com.tolimoli.pms.dto.request;

import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

/**
 * Channel Request DTO for creating/updating channels
 */
@Schema(description = "Channel request data")
public class ChannelRequest {
    
    @NotBlank(message = "Channel name is required")
    @Size(max = 100, message = "Channel name cannot exceed 100 characters")
    @Schema(description = "Channel name", example = "Booking.com", required = true)
    private String channelName;
    
    @NotBlank(message = "Channel code is required")
    @Size(max = 10, message = "Channel code cannot exceed 10 characters")
    @Pattern(regexp = "^[A-Z_]+$", message = "Channel code must contain only uppercase letters and underscores")
    @Schema(description = "Channel code", example = "BOOKING_COM", required = true)
    private String channelCode;
    
    @DecimalMin(value = "0.0", message = "Commission rate cannot be negative")
    @DecimalMax(value = "100.0", message = "Commission rate cannot exceed 100%")
    @Digits(integer = 3, fraction = 2, message = "Commission rate format is invalid")
    @Schema(description = "Commission rate percentage", example = "15.00")
    private BigDecimal commissionRate;
    
    @Size(max = 500, message = "API endpoint cannot exceed 500 characters")
    @Schema(description = "API endpoint URL", example = "https://supply-xml.booking.com")
    private String apiEndpoint;
    
    @Size(max = 500, message = "API credentials cannot exceed 500 characters")
    @Schema(description = "API credentials (encrypted)", example = "api_key_123456")
    private String apiCredentials;
    
    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    @Schema(description = "Channel description", example = "Leading online travel agency")
    private String description;
    
    @Schema(description = "Whether channel is active", example = "true")
    private Boolean isActive = true;
    
    // Constructors
    public ChannelRequest() {}
    
    public ChannelRequest(String channelName, String channelCode, BigDecimal commissionRate) {
        this.channelName = channelName;
        this.channelCode = channelCode;
        this.commissionRate = commissionRate;
    }
    
    // Getters and Setters
    public String getChannelName() { return channelName; }
    public void setChannelName(String channelName) { this.channelName = channelName; }
    
    public String getChannelCode() { return channelCode; }
    public void setChannelCode(String channelCode) { this.channelCode = channelCode; }
    
    public BigDecimal getCommissionRate() { return commissionRate; }
    public void setCommissionRate(BigDecimal commissionRate) { this.commissionRate = commissionRate; }
    
    public String getApiEndpoint() { return apiEndpoint; }
    public void setApiEndpoint(String apiEndpoint) { this.apiEndpoint = apiEndpoint; }
    
    public String getApiCredentials() { return apiCredentials; }
    public void setApiCredentials(String apiCredentials) { this.apiCredentials = apiCredentials; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
}
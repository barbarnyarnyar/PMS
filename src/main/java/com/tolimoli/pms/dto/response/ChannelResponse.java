package com.tolimoli.pms.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Channel Response DTO
 */
@Schema(description = "Channel response data")
public class ChannelResponse {
    
    @Schema(description = "Channel ID", example = "1")
    private Long id;
    
    @Schema(description = "Channel name", example = "Booking.com")
    private String channelName;
    
    @Schema(description = "Channel code", example = "BOOKING_COM")
    private String channelCode;
    
    @Schema(description = "Commission rate percentage", example = "15.00")
    private BigDecimal commissionRate;
    
    @Schema(description = "API endpoint URL", example = "https://supply-xml.booking.com")
    private String apiEndpoint;
    
    @Schema(description = "Channel description", example = "Leading online travel agency")
    private String description;
    
    @Schema(description = "Whether channel is active", example = "true")
    private Boolean isActive;
    
    @Schema(description = "Whether channel has API integration", example = "true")
    private Boolean hasApiIntegration;
    
    @Schema(description = "Whether channel is direct booking", example = "false")
    private Boolean isDirect;
    
    @Schema(description = "Creation timestamp")
    private LocalDateTime createdAt;
    
    @Schema(description = "Last update timestamp")
    private LocalDateTime updatedAt;
    
    // Constructors
    public ChannelResponse() {}
    
    public ChannelResponse(Long id, String channelName, String channelCode, 
                          BigDecimal commissionRate, Boolean isActive) {
        this.id = id;
        this.channelName = channelName;
        this.channelCode = channelCode;
        this.commissionRate = commissionRate;
        this.isActive = isActive;
        this.isDirect = commissionRate == null || commissionRate.compareTo(BigDecimal.ZERO) == 0;
        this.hasApiIntegration = apiEndpoint != null && !apiEndpoint.trim().isEmpty();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getChannelName() { return channelName; }
    public void setChannelName(String channelName) { this.channelName = channelName; }
    
    public String getChannelCode() { return channelCode; }
    public void setChannelCode(String channelCode) { this.channelCode = channelCode; }
    
    public BigDecimal getCommissionRate() { return commissionRate; }
    public void setCommissionRate(BigDecimal commissionRate) { this.commissionRate = commissionRate; }
    
    public String getApiEndpoint() { return apiEndpoint; }
    public void setApiEndpoint(String apiEndpoint) { 
        this.apiEndpoint = apiEndpoint;
        this.hasApiIntegration = apiEndpoint != null && !apiEndpoint.trim().isEmpty();
    }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    
    public Boolean getHasApiIntegration() { return hasApiIntegration; }
    public void setHasApiIntegration(Boolean hasApiIntegration) { this.hasApiIntegration = hasApiIntegration; }
    
    public Boolean getIsDirect() { return isDirect; }
    public void setIsDirect(Boolean isDirect) { this.isDirect = isDirect; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
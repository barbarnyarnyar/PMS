package com.tolimoli.pms.dto.response;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Channel Performance Response DTO
 */
@Schema(description = "Channel performance metrics")
public class ChannelPerformanceResponse {
    
    @Schema(description = "Channel ID", example = "1")
    private Long channelId;
    
    @Schema(description = "Channel name", example = "Booking.com")
    private String channelName;
    
    @Schema(description = "Channel code", example = "BOOKING_COM")
    private String channelCode;
    
    @Schema(description = "Total bookings count", example = "150")
    private Long totalBookings;
    
    @Schema(description = "Total revenue", example = "75000.00")
    private BigDecimal totalRevenue;
    
    @Schema(description = "Average booking value", example = "500.00")
    private BigDecimal averageBookingValue;
    
    @Schema(description = "Total commission earned", example = "11250.00")
    private BigDecimal totalCommission;
    
    @Schema(description = "Net revenue after commission", example = "63750.00")
    private BigDecimal netRevenue;
    
    @Schema(description = "Commission rate percentage", example = "15.00")
    private BigDecimal commissionRate;
    
    @Schema(description = "Market share percentage", example = "25.50")
    private BigDecimal marketShare;
    
    @Schema(description = "Conversion rate percentage", example = "12.30")
    private BigDecimal conversionRate;
    
    @Schema(description = "Analysis start date")
    private java.time.LocalDate startDate;
    
    @Schema(description = "Analysis end date")
    private java.time.LocalDate endDate;
    
    // Constructors
    public ChannelPerformanceResponse() {}
    
    public ChannelPerformanceResponse(Long channelId, String channelName, String channelCode,
                                    Long totalBookings, BigDecimal totalRevenue, 
                                    BigDecimal commissionRate) {
        this.channelId = channelId;
        this.channelName = channelName;
        this.channelCode = channelCode;
        this.totalBookings = totalBookings;
        this.totalRevenue = totalRevenue;
        this.commissionRate = commissionRate;
        
        // Calculate derived values
        if (totalBookings > 0 && totalRevenue != null) {
            this.averageBookingValue = totalRevenue.divide(new BigDecimal(totalBookings), 2, BigDecimal.ROUND_HALF_UP);
        }
        
        if (totalRevenue != null && commissionRate != null) {
            this.totalCommission = totalRevenue.multiply(commissionRate).divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP);
            this.netRevenue = totalRevenue.subtract(this.totalCommission);
        }
    }
    
    // Getters and Setters
    public Long getChannelId() { return channelId; }
    public void setChannelId(Long channelId) { this.channelId = channelId; }
    
    public String getChannelName() { return channelName; }
    public void setChannelName(String channelName) { this.channelName = channelName; }
    
    public String getChannelCode() { return channelCode; }
    public void setChannelCode(String channelCode) { this.channelCode = channelCode; }
    
    public Long getTotalBookings() { return totalBookings; }
    public void setTotalBookings(Long totalBookings) { this.totalBookings = totalBookings; }
    
    public BigDecimal getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; }
    
    public BigDecimal getAverageBookingValue() { return averageBookingValue; }
    public void setAverageBookingValue(BigDecimal averageBookingValue) { this.averageBookingValue = averageBookingValue; }
    
    public BigDecimal getTotalCommission() { return totalCommission; }
    public void setTotalCommission(BigDecimal totalCommission) { this.totalCommission = totalCommission; }
    
    public BigDecimal getNetRevenue() { return netRevenue; }
    public void setNetRevenue(BigDecimal netRevenue) { this.netRevenue = netRevenue; }
    
    public BigDecimal getCommissionRate() { return commissionRate; }
    public void setCommissionRate(BigDecimal commissionRate) { this.commissionRate = commissionRate; }
    
    public BigDecimal getMarketShare() { return marketShare; }
    public void setMarketShare(BigDecimal marketShare) { this.marketShare = marketShare; }
    
    public BigDecimal getConversionRate() { return conversionRate; }
    public void setConversionRate(BigDecimal conversionRate) { this.conversionRate = conversionRate; }
    
    public java.time.LocalDate getStartDate() { return startDate; }
    public void setStartDate(java.time.LocalDate startDate) { this.startDate = startDate; }
    
    public java.time.LocalDate getEndDate() { return endDate; }
    public void setEndDate(java.time.LocalDate endDate) { this.endDate = endDate; }
}
package com.tolimoli.pms.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Guest Detail Response DTO - Complete guest information
 */
@Schema(description = "Complete guest information response")
public class GuestDetailResponse extends GuestResponse {
    
    @Schema(description = "ID number", example = "P1234567")
    private String idNumber;
    
    @Schema(description = "ID type", example = "PASSPORT")
    private String idType;
    
    @Schema(description = "Date of birth", example = "1990-01-15")
    private LocalDate dateOfBirth;
    
    @Schema(description = "Age", example = "34")
    private Integer age;
    
    @Schema(description = "Gender", example = "MALE")
    private String gender;
    
    @Schema(description = "Nationality", example = "American")
    private String nationality;
    
    @Schema(description = "Full address", example = "123 Main St, New York, NY 10001")
    private String fullAddress;
    
    @Schema(description = "Address", example = "123 Main St")
    private String address;
    
    @Schema(description = "City", example = "New York")
    private String city;
    
    @Schema(description = "State", example = "New York")
    private String state;
    
    @Schema(description = "Postal code", example = "10001")
    private String postalCode;
    
    @Schema(description = "Guest preferences", example = "Non-smoking room, high floor")
    private String preferences;
    
    @Schema(description = "Special requests", example = "Late check-in")
    private String specialRequests;
    
    @Schema(description = "Marketing consent", example = "true")
    private Boolean marketingConsent;
    
    @Schema(description = "Loyalty points", example = "1250")
    private Integer loyaltyPoints;
    
    @Schema(description = "Loyalty tier", example = "GOLD")
    private String loyaltyTier;
    
    @Schema(description = "Average spending per stay", example = "500.00")
    private BigDecimal averageSpendingPerStay;
    
    @Schema(description = "Preferred room type", example = "DELUXE")
    private String preferredRoomType;
    
    @Schema(description = "Last update timestamp")
    private LocalDateTime lastUpdated;
    
    // Getters and Setters
    public String getIdNumber() { return idNumber; }
    public void setIdNumber(String idNumber) { this.idNumber = idNumber; }
    
    public String getIdType() { return idType; }
    public void setIdType(String idType) { this.idType = idType; }
    
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { 
        this.dateOfBirth = dateOfBirth;
        if (dateOfBirth != null) {
            this.age = LocalDate.now().getYear() - dateOfBirth.getYear();
        }
    }
    
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    
    public String getNationality() { return nationality; }
    public void setNationality(String nationality) { this.nationality = nationality; }
    
    public String getFullAddress() { return fullAddress; }
    public void setFullAddress(String fullAddress) { this.fullAddress = fullAddress; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    
    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }
    
    public String getPreferences() { return preferences; }
    public void setPreferences(String preferences) { this.preferences = preferences; }
    
    public String getSpecialRequests() { return specialRequests; }
    public void setSpecialRequests(String specialRequests) { this.specialRequests = specialRequests; }
    
    public Boolean getMarketingConsent() { return marketingConsent; }
    public void setMarketingConsent(Boolean marketingConsent) { this.marketingConsent = marketingConsent; }
    
    public Integer getLoyaltyPoints() { return loyaltyPoints; }
    public void setLoyaltyPoints(Integer loyaltyPoints) { this.loyaltyPoints = loyaltyPoints; }
    
    public String getLoyaltyTier() { return loyaltyTier; }
    public void setLoyaltyTier(String loyaltyTier) { this.loyaltyTier = loyaltyTier; }
    
    public BigDecimal getAverageSpendingPerStay() { return averageSpendingPerStay; }
    public void setAverageSpendingPerStay(BigDecimal averageSpendingPerStay) { this.averageSpendingPerStay = averageSpendingPerStay; }
    
    public String getPreferredRoomType() { return preferredRoomType; }
    public void setPreferredRoomType(String preferredRoomType) { this.preferredRoomType = preferredRoomType; }
    
    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }
}
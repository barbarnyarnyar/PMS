// ===== RESPONSE DTOs =====

package com.tolimoli.pms.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Guest Response DTO - Basic guest information
 */
@Schema(description = "Guest basic information response")
public class GuestResponse {

  @Schema(description = "Guest ID", example = "1")
  private Long id;

  @Schema(description = "First name", example = "John")
  private String firstName;

  @Schema(description = "Last name", example = "Smith")
  private String lastName;

  @Schema(description = "Full name", example = "John Smith")
  private String fullName;

  @Schema(description = "Email address", example = "john.smith@email.com")
  private String email;

  @Schema(description = "Phone number", example = "+1234567890")
  private String phone;

  @Schema(description = "Guest type", example = "INDIVIDUAL")
  private String guestType;

  @Schema(description = "Country", example = "United States")
  private String country;

  @Schema(description = "Total stays", example = "5")
  private Integer totalStays;

  @Schema(description = "Total spent", example = "2500.00")
  private BigDecimal totalSpent;

  @Schema(description = "Last stay date", example = "2024-01-15")
  private LocalDate lastStayDate;

  @Schema(description = "Registration date")
  private LocalDateTime registrationDate;

  @Schema(description = "Is VIP guest", example = "false")
  private Boolean isVip;

  @Schema(description = "Is active", example = "true")
  private Boolean isActive;

  // Constructors
  public GuestResponse() {
  }

  public GuestResponse(Long id, String firstName, String lastName, String email) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.fullName = firstName + " " + lastName;
    this.email = email;
  }

  // Getters and Setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
    updateFullName();
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
    updateFullName();
  }

  public String getFullName() {
    return fullName;
  }

  private void updateFullName() {
    this.fullName = (firstName != null ? firstName : "") + " " + (lastName != null ? lastName : "");
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getGuestType() {
    return guestType;
  }

  public void setGuestType(String guestType) {
    this.guestType = guestType;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public Integer getTotalStays() {
    return totalStays;
  }

  public void setTotalStays(Integer totalStays) {
    this.totalStays = totalStays;
  }

  public BigDecimal getTotalSpent() {
    return totalSpent;
  }

  public void setTotalSpent(BigDecimal totalSpent) {
    this.totalSpent = totalSpent;
  }

  public LocalDate getLastStayDate() {
    return lastStayDate;
  }

  public void setLastStayDate(LocalDate lastStayDate) {
    this.lastStayDate = lastStayDate;
  }

  public LocalDateTime getRegistrationDate() {
    return registrationDate;
  }

  public void setRegistrationDate(LocalDateTime registrationDate) {
    this.registrationDate = registrationDate;
  }

  public Boolean getIsVip() {
    return isVip;
  }

  public void setIsVip(Boolean isVip) {
    this.isVip = isVip;
  }

  public Boolean getIsActive() {
    return isActive;
  }

  public void setIsActive(Boolean isActive) {
    this.isActive = isActive;
  }
}
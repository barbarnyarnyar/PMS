package com.tolimoli.pms.dto.request;

import javax.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Guest Request DTO for creating new guests
 */
@Schema(description = "Guest creation request")
public class GuestRequest {

  @NotBlank(message = "First name is required")
  @Size(max = 100, message = "First name cannot exceed 100 characters")
  @Schema(description = "Language preference", example = "English")
  private String languagePreference;

  @Schema(description = "Special occasions", example = "Anniversary, Birthday")
  private String specialOccasions;

  @Schema(description = "Preferred check-in time", example = "15:00")
  private String preferredCheckInTime;

  @Schema(description = "Preferred check-out time", example = "11:00")
  private String preferredCheckOutTime;

  @Schema(description = "Additional notes", example = "Prefers ground floor rooms")
  private String additionalNotes;

  private String roomPreferences;
  private String dietaryRestrictions;
  private String accessibilityNeeds;
  private String communicationPreferences;

  private String firstName;
  private String lastName;
  private String email;
  private String phone;
  private String idType;

  public GuestRequest() {
  }

  public GuestRequest(String firstName, String lastName, String email, String phone, String idType) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.phone = phone;
    this.idType = idType;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
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

  public String getIdType() {
    return idType;
  }

  public void setIdType(String idType) {
    this.idType = idType;
  }

  // Getters and Setters
  public String getRoomPreferences() {
    return roomPreferences;
  }

  public void setRoomPreferences(String roomPreferences) {
    this.roomPreferences = roomPreferences;
  }

  public String getDietaryRestrictions() {
    return dietaryRestrictions;
  }

  public void setDietaryRestrictions(String dietaryRestrictions) {
    this.dietaryRestrictions = dietaryRestrictions;
  }

  public String getAccessibilityNeeds() {
    return accessibilityNeeds;
  }

  public void setAccessibilityNeeds(String accessibilityNeeds) {
    this.accessibilityNeeds = accessibilityNeeds;
  }

  public String getCommunicationPreferences() {
    return communicationPreferences;
  }

  public void setCommunicationPreferences(String communicationPreferences) {
    this.communicationPreferences = communicationPreferences;
  }

  public String getLanguagePreference() {
    return languagePreference;
  }

  public void setLanguagePreference(String languagePreference) {
    this.languagePreference = languagePreference;
  }

  public String getSpecialOccasions() {
    return specialOccasions;
  }

  public void setSpecialOccasions(String specialOccasions) {
    this.specialOccasions = specialOccasions;
  }

  public String getPreferredCheckInTime() {
    return preferredCheckInTime;
  }

  public void setPreferredCheckInTime(String preferredCheckInTime) {
    this.preferredCheckInTime = preferredCheckInTime;
  }

  public String getPreferredCheckOutTime() {
    return preferredCheckOutTime;
  }

  public void setPreferredCheckOutTime(String preferredCheckOutTime) {
    this.preferredCheckOutTime = preferredCheckOutTime;
  }

  public String getAdditionalNotes() {
    return additionalNotes;
  }

  public void setAdditionalNotes(String additionalNotes) {
    this.additionalNotes = additionalNotes;
  }
}
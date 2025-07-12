package com.tolimoli.pms.dto.request;

import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

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
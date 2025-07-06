package com.tolimoli.pms.entity;

// ===== ENUMS =====
public enum ReservationStatus {
  CONFIRMED("Confirmed"),
  CHECKED_IN("Checked In"),
  CHECKED_OUT("Checked Out"),
  CANCELLED("Cancelled"),
  NO_SHOW("No Show"),
  WAITLISTED("Wait Listed");

  private final String displayName;

  ReservationStatus(String displayName) {
    this.displayName = displayName;
  }

  public String getDisplayName() {
    return displayName;
  }
}

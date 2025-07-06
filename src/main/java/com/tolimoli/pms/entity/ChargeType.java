package com.tolimoli.pms.entity;

public enum ChargeType {
  ROOM("Room Accommodation"),
  TAX("Taxes and Fees"),
  SERVICE("Hotel Services"),
  EXTRA("Additional Amenities"),
  PENALTY("Penalties and Fines"),
  DISCOUNT("Discounts and Promotions");

  private final String description;

  ChargeType(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }
}

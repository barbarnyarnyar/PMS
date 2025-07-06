package com.tolimoli.pms.entity;

// ===== ENUMS =====
enum RoomType {
  SINGLE, DOUBLE, TWIN, SUITE, DELUXE
}

enum RoomStatus {
  AVAILABLE, OCCUPIED, DIRTY, MAINTENANCE, OUT_OF_ORDER
}

enum ReservationStatus {
  CONFIRMED, CHECKED_IN, CHECKED_OUT, CANCELLED, NO_SHOW
}

enum ChargeType {
  ROOM, TAX, SERVICE, EXTRA, PENALTY
}

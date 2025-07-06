package com.tolimoli.pms.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tolimoli.pms.entity.Guest;
import com.tolimoli.pms.entity.IdType;
import com.tolimoli.pms.entity.Reservation;
import com.tolimoli.pms.repository.GuestRepository;

import jakarta.transaction.Transactional;

// ===== 2. GUEST SERVICE =====
@Service
@Transactional
public class GuestService {

  @Autowired
  private GuestRepository guestRepository;

  // Create guest
  public Guest createGuest(String firstName, String lastName, String email, String phone,
      String idNumber, IdType idType) {
    // Check if guest already exists
    Optional<Guest> existingGuest = guestRepository.findByEmail(email);
    if (existingGuest.isPresent()) {
      return existingGuest.get(); // Return existing guest
    }

    Guest guest = new Guest();
    guest.setFirstName(firstName);
    guest.setLastName(lastName);
    guest.setEmail(email);
    guest.setPhone(phone);
    guest.setIdNumber(idNumber);
    guest.setIdType(idType);

    return guestRepository.save(guest);
  }

  // Find guest by email
  public Optional<Guest> findGuestByEmail(String email) {
    return guestRepository.findByEmail(email);
  }

  // Get guest history
  public List<Reservation> getGuestHistory(Long guestId) {
    return guestRepository.findGuestReservations(guestId);
  }

  // Update guest info
  public Guest updateGuest(Long guestId, String firstName, String lastName,
      String phone, String address) {
    Guest guest = guestRepository.findById(guestId)
        .orElseThrow(() -> new RuntimeException("Guest not found"));

    guest.setFirstName(firstName);
    guest.setLastName(lastName);
    guest.setPhone(phone);
    guest.setAddress(address);

    return guestRepository.save(guest);
  }
}
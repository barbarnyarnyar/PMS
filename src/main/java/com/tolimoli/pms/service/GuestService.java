package com.tolimoli.pms.service;

import com.tolimoli.pms.entity.Guest;
import com.tolimoli.pms.repository.GuestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class GuestService {

    @Autowired
    private GuestRepository guestRepository;

    /**
     * Creates a new guest.
     * @param guestRequest DTO with new guest information.
     * @return DTO of the created guest.
     */
    // public GuestResponse createGuest(GuestRequest guestRequest) {
    //     // Check if a guest with the same email already exists
    //     if (guestRepository.existsByEmail(guestRequest.getEmail())) {
    //         throw new BusinessLogicException("A guest with email " + guestRequest.getEmail() + " already exists.");
    //     }

    //     // Map DTO to entity. Assumes GuestRequest has corresponding getters.
    //     Guest guest = new Guest();
    //     guest.setFirstName(guestRequest.getFirstName());
    //     guest.setLastName(guestRequest.getLastName());
    //     guest.setEmail(guestRequest.getEmail());
    //     guest.setPhone(guestRequest.getPhone());
    //     guest.setIdType(guestRequest.getIdType());
    //     guest.setIdNumber(guestRequest.getIdNumber());

    //     Guest savedGuest = guestRepository.save(guest);

    //     // Map entity to response DTO.
    //     return mapToGuestResponse(savedGuest);
    // }

    /**
     * Finds a guest by their email address.
     * This method is required by ReservationService.
     * @param email The email of the guest to find.
     * @return An Optional containing the Guest if found.
     */
    public Optional<Guest> findGuestByEmail(String email) {
        return guestRepository.findByEmail(email);
    }

    /**
     * Helper method to map a Guest entity to a GuestResponse DTO.
     */
    // private GuestResponse mapToGuestResponse(Guest guest) {
    //     // This mapping logic could also be in a dedicated mapper class.
    //     // Assumes GuestResponse has setters for these fields.
    //     return new GuestResponse(guest.getId(), guest.getFirstName(), guest.getLastName(), guest.getEmail(), guest.getPhone());
    // }

    // Other methods to implement the rest of GuestController's endpoints would go here.
}
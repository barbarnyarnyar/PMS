package com.tolimoli.pms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tolimoli.pms.entity.Guest;
import com.tolimoli.pms.entity.Reservation;

public interface GuestRepository extends JpaRepository<Guest, Long>,
    JpaSpecificationExecutor<Guest> {

    Optional<Guest> findByEmail(String email);

    List<Reservation> findGuestReservations(Long guestId);

    List<Guest> findByLastName(String lastName);

    List<Guest> findByCity(String city);

    List<Guest> findByCountry(String country);

    List<Guest> findByFirstNameAndLastName(String firstName, String lastName);

    List<Guest> findByFirstName(String firstName);

    List<Guest> findByPhone(String phone);

    List<Guest> findByEmailAndPhone(String email, String phone);

    List<Guest> findByIdNumber(String idNumber); 
}

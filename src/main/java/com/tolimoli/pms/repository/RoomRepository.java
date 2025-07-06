package com.tolimoli.pms.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tolimoli.pms.entity.Room;
import com.tolimoli.pms.entity.RoomType;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long>,
    JpaSpecificationExecutor<Room> {

    @Query("""
        SELECT r FROM Room r
            WHERE r.roomType = :roomType
            AND r.id NOT IN (
                SELECT b.room.id FROM Booking b
                WHERE b.checkIn < :checkOut AND b.checkOut > :checkIn
            )
    """)
    java.util.List<Room> findAvailableRooms(LocalDate checkIn, LocalDate checkOut, RoomType roomType); 

    
    Optional<Room> findByRoomNumber(String roomNumber);
}

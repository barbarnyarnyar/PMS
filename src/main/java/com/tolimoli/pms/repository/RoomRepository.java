package com.tolimoli.pms.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tolimoli.pms.entity.Room;
import com.tolimoli.pms.entity.RoomType;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long>,
    JpaSpecificationExecutor<Room> {

    /**
     * Find available rooms for given date range and room type
     * Excludes rooms that have confirmed or checked-in reservations overlapping with the date range
     */
    @Query("SELECT r FROM Room r WHERE r.roomType = :roomType " +
           "AND r.isActive = true " +
           "AND r.status = 'AVAILABLE' " +
           "AND r.id NOT IN (" +
           "    SELECT res.room.id FROM Reservation res " +
           "    WHERE res.status IN ('CONFIRMED', 'CHECKED_IN') " +
           "    AND res.checkInDate < :checkOutDate " +
           "    AND res.checkOutDate > :checkInDate" +
           ")")
    List<Room> findAvailableRooms(@Param("checkInDate") LocalDate checkInDate, 
                                  @Param("checkOutDate") LocalDate checkOutDate, 
                                  @Param("roomType") RoomType roomType); 

    /**
     * Find room by room number
     */
    Optional<Room> findByRoomNumber(String roomNumber);
    
    /**
     * Find all rooms by room type
     */
    List<Room> findByRoomType(RoomType roomType);
    
    /**
     * Find all available rooms (status = AVAILABLE)
     */
    @Query("SELECT r FROM Room r WHERE r.status = 'AVAILABLE' AND r.isActive = true")
    List<Room> findAllAvailableRooms();
    
    /**
     * Find rooms by status
     */
    @Query("SELECT r FROM Room r WHERE r.status = :status")
    List<Room> findByStatus(@Param("status") String status);
    
    /**
     * Find active rooms
     */
    List<Room> findByIsActiveTrue();
    
    /**
     * Count rooms by type
     */
    @Query("SELECT COUNT(r) FROM Room r WHERE r.roomType = :roomType AND r.isActive = true")
    long countByRoomType(@Param("roomType") RoomType roomType);
}

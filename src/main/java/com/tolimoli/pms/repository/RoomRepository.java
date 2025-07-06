package com.tolimoli.pms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.tolimoli.pms.entity.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long>,
    JpaSpecificationExecutor<Room> {
  
}

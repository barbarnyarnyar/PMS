package com.tolimoli.pms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tolimoli.pms.entity.Guest;

public interface GuestRepository extends JpaRepository<Guest, Long>,
    JpaSpecificationExecutor<Guest> {

}

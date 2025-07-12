package com.tolimoli.pms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.tolimoli.pms.entity.FolioCharge;

@Repository
public interface FolioChargeRepository extends JpaRepository<FolioCharge, Long>,
        JpaSpecificationExecutor<FolioCharge> {

    List<FolioCharge> findByReservationId(Long reservationId);

    List<FolioCharge> findByReservationIdAndIsPaidFalse(Long reservationId);
}

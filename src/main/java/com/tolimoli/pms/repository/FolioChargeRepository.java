package com.tolimoli.pms.repository;

// import java.math.BigDecimal;
// import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tolimoli.pms.entity.FolioCharge;

public interface FolioChargeRepository extends JpaRepository<FolioCharge, Long>,
        JpaSpecificationExecutor<FolioCharge> {

    List<FolioCharge> findByReservationId(Long reservationId);

    List<FolioCharge> findByReservationIdAndIsPaidFalse(Long reservationId);
}

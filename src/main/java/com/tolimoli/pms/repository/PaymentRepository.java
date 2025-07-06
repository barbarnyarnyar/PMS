package com.tolimoli.pms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tolimoli.pms.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long>,
    JpaSpecificationExecutor<Payment> {

    List<Payment> findByReservationId(Long reservationId);
}

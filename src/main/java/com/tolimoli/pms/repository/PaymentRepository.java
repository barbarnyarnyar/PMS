package com.tolimoli.pms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tolimoli.pms.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long>,
    JpaSpecificationExecutor<Payment> {

}

package com.tolimoli.pms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tolimoli.pms.entity.FolioCharge;

public interface FolioChargeRepository extends JpaRepository<FolioCharge, Long>,
    JpaSpecificationExecutor<FolioCharge> {

}

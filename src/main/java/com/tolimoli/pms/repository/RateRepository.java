package com.tolimoli.pms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tolimoli.pms.entity.Rate;

public interface RateRepository extends JpaRepository<Rate, Long>,
    JpaSpecificationExecutor<Rate> {

}

package com.tolimoli.pms.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tolimoli.pms.entity.Rate;

public interface RateRepository extends JpaRepository<Rate, Long>,
        JpaSpecificationExecutor<Rate> {

    List<Rate> findByChannelIdAndRateDate(Long channelId, LocalDate date);
}

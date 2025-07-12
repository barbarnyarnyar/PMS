package com.tolimoli.pms.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.tolimoli.pms.entity.Rate;

@Repository
public interface RateRepository extends JpaRepository<Rate, Long>,
        JpaSpecificationExecutor<Rate> {

    List<Rate> findByChannelIdAndRateDate(Long channelId, LocalDate date);

    Optional<Rate> findByRoomIdAndChannelIdAndRateDate(Long roomId, Long channelId, LocalDate date);

    List<Rate> findByRoomIdAndRateDateBetween(Long roomId, LocalDate startDate, LocalDate endDate);
}

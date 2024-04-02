package com.sideProject.PlanIT.domain.reservation.service;

import com.sideProject.PlanIT.domain.reservation.controller.ENUM.ReservationFindOption;
import com.sideProject.PlanIT.domain.reservation.dto.response.ReservationResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public interface ReservationService {
    String changeAvailability(LocalDate date,List<LocalTime> times, Long userId);
    String reservation(Long reservationId, Long userId, Long programId, LocalDateTime now);
    Map<LocalDate, List<ReservationResponse>> findReservationForWeekByMember(LocalDate day, Long id, ReservationFindOption option);
    List<ReservationResponse> findReservationForDayByEmployee(LocalDate day, Long id);
    String cancel(Long userId, Long reservationId, LocalDateTime now);
}

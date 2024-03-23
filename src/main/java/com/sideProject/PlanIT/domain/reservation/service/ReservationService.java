package com.sideProject.PlanIT.domain.reservation.service;

import com.sideProject.PlanIT.domain.reservation.dto.response.ReservationResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface ReservationService {
    String changeAvailability(List<LocalDateTime> times, Long employeeId, Long userId);
    String reservation(Long reservationId, Long userId, Long programId);
    Map<LocalDate, List<ReservationResponse>> findReservationForWeekByMember(LocalDate day, Long id);
    List<ReservationResponse> findReservationForWeekByEmployee(LocalDate day, Long id);
    String cancel(Long userId, Long reservationId);
}

package com.sideProject.PlanIT.domain.reservation.controller;

import com.sideProject.PlanIT.common.response.ApiResponse;
import com.sideProject.PlanIT.domain.reservation.controller.ENUM.ReservationFindOption;
import com.sideProject.PlanIT.domain.reservation.dto.reqeust.ChangeReservationRequestDto;
import com.sideProject.PlanIT.domain.reservation.dto.reqeust.ReservationRequestDto;
import com.sideProject.PlanIT.domain.reservation.dto.response.ReservationResponseDto;
import com.sideProject.PlanIT.domain.reservation.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/reservation")
public class ReservationController {
    private final ReservationService reservationService;

    @PutMapping("/change")
    public ApiResponse<String> changeAvailability(
            Principal principal,
            @Valid @RequestBody ChangeReservationRequestDto request
    ) {
        return ApiResponse.ok(
                reservationService.changeAvailability(
                        request.getReservationDate(),
                        request.getReservationTimes(),
                        Long.valueOf(principal.getName())
                )
        );
    }

    @PostMapping("/{reservationId}")
    public ApiResponse<String> reservation(
            Principal principal,
            @PathVariable("reservationId") Long reservationId,
            @Valid @RequestBody ReservationRequestDto request
    ) {
        LocalDateTime now = LocalDateTime.now();
        return ApiResponse.ok(
                reservationService.reservation(
                        reservationId,
                        Long.valueOf(principal.getName()),
                        request.getProgramId(),
                        now
                )
        );
    }

    @GetMapping("")
    public ApiResponse<Map<LocalDate, List<ReservationResponseDto>>> findReservation(
            @RequestParam(value = "date", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(value = "option", defaultValue = "ALL") ReservationFindOption option,
            Principal principal
    ) {
        if (date == null) {
            date = LocalDate.now(); // 파라미터가 없을 경우 기본값으로 오늘 날짜를 사용
        }
        return ApiResponse.ok(
                reservationService.findReservationForWeekByMember(
                        date,
                        Long.valueOf(principal.getName()),
                        option
                )
        );
    }

    @GetMapping("/trainer/{employeeId}")
    public ApiResponse<List<ReservationResponseDto>> findReservationByEmployee(
            @RequestParam(value = "date", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @PathVariable("employeeId") Long employeeId
    ) {
        if (date == null) {
            date = LocalDate.now(); // 파라미터가 없을 경우 기본값으로 오늘 날짜를 사용
        }
        return ApiResponse.ok(
                reservationService.findReservationForDayByEmployee(
                        date,
                        employeeId
                )
        );
    }

    @DeleteMapping("/{reservationId}")
    public ApiResponse<String> cancelReservation(
            @PathVariable("reservationId") Long reservationId,
            Principal principal
    ) {
        LocalDateTime now = LocalDateTime.now();

        return ApiResponse.ok(
                reservationService.cancel(
                        Long.valueOf(principal.getName()),
                        reservationId,
                        now
                )
        );
    }
}

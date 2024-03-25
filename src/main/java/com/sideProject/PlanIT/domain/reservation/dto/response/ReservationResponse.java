package com.sideProject.PlanIT.domain.reservation.dto.response;

import com.sideProject.PlanIT.domain.reservation.entity.ENUM.ReservationStatus;
import com.sideProject.PlanIT.domain.reservation.entity.Reservation;
import com.sideProject.PlanIT.domain.user.dto.member.response.EmployeeSemiResponseDto;
import com.sideProject.PlanIT.domain.user.dto.member.response.MemberSemiResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Slf4j
@Getter
public class ReservationResponse {
    Long id;
    MemberSemiResponseDto member;
    EmployeeSemiResponseDto employee;
    Long programId;
    String programName;
    LocalDateTime reservationTime;
    LocalTime programTime;
    ReservationStatus status;

    @Builder
    public ReservationResponse(
            Long id,
            MemberSemiResponseDto member,
            EmployeeSemiResponseDto employee,
            Long programId,
            String programName,
            LocalDateTime reservationTime,
            LocalTime programTime,
            ReservationStatus status
    ) {
        this.id = id;
        this.member = member;
        this.employee = employee;
        this.programId = programId;
        this.programName = programName;
        this.reservationTime = reservationTime;
        this.programTime = programTime;
        this.status = status;
    }

    public static ReservationResponse of(Reservation reservation) {
        if(reservation.getStatus() == ReservationStatus.POSSIBLE) {
            return ReservationResponse.builder()
                    .id(reservation.getId())
                    .employee(EmployeeSemiResponseDto.of(reservation.getEmployee()))
                    .status(reservation.getStatus())
                    .build();
        }

        return ReservationResponse.builder()
                .id(reservation.getId())
                .member(MemberSemiResponseDto.of(reservation.getMember()))
                .employee(EmployeeSemiResponseDto.of(reservation.getEmployee()))
                .programId(reservation.getProgram().getId())
                .programName(reservation.getProgram().getProduct().getName())
                .reservationTime(reservation.getReservedTime())
                .programTime(reservation.getClassTime())
                .status(reservation.getStatus())
                .build();
    }
}

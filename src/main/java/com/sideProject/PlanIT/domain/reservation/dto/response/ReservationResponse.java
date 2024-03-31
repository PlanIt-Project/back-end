package com.sideProject.PlanIT.domain.reservation.dto.response;

import com.sideProject.PlanIT.domain.product.entity.Product;
import com.sideProject.PlanIT.domain.program.entity.Program;
import com.sideProject.PlanIT.domain.reservation.entity.ENUM.ReservationStatus;
import com.sideProject.PlanIT.domain.reservation.entity.Reservation;
import com.sideProject.PlanIT.domain.user.dto.member.response.EmployeeSemiResponseDto;
import com.sideProject.PlanIT.domain.user.dto.member.response.MemberSemiResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

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
                    .reservationTime(reservation.getReservedTime())
                    .employee(EmployeeSemiResponseDto.of(reservation.getEmployee()))
                    .status(reservation.getStatus())
                    .build();
        }

        return ReservationResponse.builder()
                .id(reservation.getId())
                .member(Optional.ofNullable(reservation.getMember())
                        .map(MemberSemiResponseDto::of)
                        .orElse(null))
                .employee(EmployeeSemiResponseDto.of(reservation.getEmployee()))
                .programId(Optional.ofNullable(reservation.getProgram())
                                .map(Program::getId)
                                .orElse(null))
                .programName(Optional.ofNullable(reservation.getProgram())
                        .map(Program::getProduct)
                        .map(Product::getName)
                        .orElse(null))
                .reservationTime(reservation.getReservedTime())
                .programTime(reservation.getClassTime())
                .status(reservation.getStatus())
                .build();
    }
}

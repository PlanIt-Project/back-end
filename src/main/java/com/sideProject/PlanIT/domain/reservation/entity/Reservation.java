package com.sideProject.PlanIT.domain.reservation.entity;

import com.sideProject.PlanIT.common.response.CustomException;
import com.sideProject.PlanIT.common.response.ErrorCode;
import com.sideProject.PlanIT.domain.program.entity.Program;
import com.sideProject.PlanIT.domain.reservation.entity.ENUM.ReservationStatus;
import com.sideProject.PlanIT.domain.user.entity.Employee;
import com.sideProject.PlanIT.domain.user.entity.Member;
import com.sideProject.PlanIT.domain.user.entity.WorkTime;
import com.sideProject.PlanIT.domain.user.entity.enums.Week;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@Slf4j
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Reservation {
    private static final int RESERVATION_THRESHOLD_MINUTES = 10;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column
    private LocalDateTime reservedTime;

    @Column
    private LocalTime classTime;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    @ManyToOne
    @JoinColumn(name = "program_id")
    private Program program;

    @ManyToOne
    @JoinColumn(name = "employ_id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Reservation(
            LocalDateTime reservedTime,
            LocalTime classTime,
            ReservationStatus status,
            Employee employee) {
        this.reservedTime = reservedTime;
        this.classTime = classTime;
        this.status = status;
        this.employee = employee;
    }
    public void reservation(Program program, Member member, LocalDateTime now) {
        ensureReservationIsPossible(now);

        this.member = member;
        this.program = program;
        this.status = ReservationStatus.RESERVED;
    }

    public void cancel(LocalDateTime now) {
        ensureCancellationIsPossible(now);

        this.program = null;
        this.member = null;
        this.status = ReservationStatus.POSSIBLE;
    }

    public void finish() {
        this.status = ReservationStatus.FINISHED;
    }

    private void ensureReservationIsPossible(LocalDateTime now) {
        if (status != ReservationStatus.POSSIBLE) {
            throw new CustomException("예약 " + id + "은 예약할 수 없습니다.", ErrorCode.ALREADY_RESERVATION);
        }

        if (isPastReservationThreshold(now)) {
            throw new CustomException("예약 " + id + "은 예약 가능 시간이 지났습니다.", ErrorCode.INVALID_RESERVATION_TIME);
        }
    }

    private void ensureCancellationIsPossible(LocalDateTime now) {
        if (isPastReservationThreshold(now)) {
            throw new CustomException("예약 " + id + "은 예약 취소시간이 지났습니다.", ErrorCode.INVALID_RESERVATION_TIME);
        }
    }

    private boolean isPastReservationThreshold(LocalDateTime now) {
        return now.isAfter(reservedTime.minusMinutes(RESERVATION_THRESHOLD_MINUTES));
    }

    // 예약 시간이 출근 시간 안에 포함 되는지 체크
    public boolean isWithinEmployeeWorkTime(List<WorkTime> employeeWorkTimes) {
        LocalDate reservationDate = reservedTime.toLocalDate();
        LocalTime reservationTime = reservedTime.toLocalTime();

        for (WorkTime workTime : employeeWorkTimes) {
            if (workTime.getWeek().getDayOfWeek() == reservationDate.getDayOfWeek()) {
                if ( isWithinTime(workTime.getStartAt(), workTime.getEndAt(), reservationTime) ) {
                    return true; // 예약 시간이 직원의 근무 시간 내
                }
            }
        }
        return false; // 근무 시간 외
    }

    // 특정 시간이 사이에 존재하는지 확인
    private boolean isWithinTime(LocalTime start, LocalTime end, LocalTime reservedTime) {
        return (reservedTime.isAfter(start) && reservedTime.isBefore(end)) || reservedTime.equals(start) || reservedTime.equals(end);
    }
}

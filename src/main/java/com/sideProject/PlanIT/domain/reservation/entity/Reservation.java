package com.sideProject.PlanIT.domain.reservation.entity;

import com.sideProject.PlanIT.common.response.CustomException;
import com.sideProject.PlanIT.common.response.ErrorCode;
import com.sideProject.PlanIT.domain.program.entity.Program;
import com.sideProject.PlanIT.domain.reservation.entity.ENUM.ReservationStatus;
import com.sideProject.PlanIT.domain.user.entity.Employee;
import com.sideProject.PlanIT.domain.user.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.catalina.User;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
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

}

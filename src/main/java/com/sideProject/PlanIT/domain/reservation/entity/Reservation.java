package com.sideProject.PlanIT.domain.reservation.entity;

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

    public void reservation(Program program, Member member) {
        this.member = member;
        this.program = program;
        this.status = ReservationStatus.RESERVED;
    }

    public void cancel() {
        this.program = null;
        this.member = null;
        this.status = ReservationStatus.POSSIBLE;
    }
}

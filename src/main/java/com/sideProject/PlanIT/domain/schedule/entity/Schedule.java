package com.sideProject.PlanIT.domain.schedule.entity;

import com.sideProject.PlanIT.domain.program.entity.Program;
import com.sideProject.PlanIT.domain.schedule.entity.ENUM.ScheduleAttendance;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "schedule_name")
    private String name;

    @Column(name = "schedule_detail")
    private String detail;

    @Column
    private LocalDateTime startAt;

    @Column
    private LocalDateTime endAt;

    @Enumerated(EnumType.STRING)
    private ScheduleAttendance attendance;

    @ManyToOne
    @JoinColumn(name = "program_id")
    private Program program;

    @Builder
    public Schedule(String name, String detail, LocalDateTime startAt, LocalDateTime endAt, ScheduleAttendance attendance, Program program) {
        this.name = name;
        this.detail = detail;
        this.startAt = startAt;
        this.endAt = endAt;
        this.attendance = attendance;
        this.program = program;
    }
}

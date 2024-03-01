package com.sideProject.PlanIT.domain.schedule;

import com.sideProject.PlanIT.domain.program.entity.Program;
import com.sideProject.PlanIT.domain.schedule.ENUM.ScheduleAttendance;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long Id;

    @Column(name = "schedule_name")
    private String name;

    @Column(name = "schedule_detail")
    private String detail;

    @Column
    private LocalDateTime dateTime;

    @Enumerated(EnumType.STRING)
    private ScheduleAttendance attendance;

    @ManyToOne
    @JoinColumn(name = "program_id")
    private Program program;
}

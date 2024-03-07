package com.sideProject.PlanIT.domain.schedule.entity;

import com.sideProject.PlanIT.domain.program.entity.Program;
import com.sideProject.PlanIT.domain.schedule.entity.ENUM.ScheduleAttendance;
import com.sideProject.PlanIT.domain.user.entity.Employee;
import com.sideProject.PlanIT.domain.user.entity.Member;
import jakarta.persistence.*;

import java.time.LocalDateTime;


/*todo: 스케줄 드리블 방식 선정
1. 직원 스케줄 테이블에 컬럼 (1교시, 2교시, 3교시 ..) 에 Boolean 타입 삽입 => 하루가 하나의 데이터
2. 직원 스케줄 테이블에 컬럼 (날짜, 시간, 가능여부) 로 삽입 => 한 타임이 하나의 데이터
3. 직원 스케줄 테이블에 컬럼 (가능여부) 만 삽입, 시간대별 가능 여부를 이진수로 표현 => 하루가 하나의 데이터
3_1. 예시) 3월 5일(8~10: 가능, 10~12: 불가능, 12~14: 가능, 14~16: 가능, 16~18: 가능) => 10111로 삽입
 */
@Entity
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
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
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
}

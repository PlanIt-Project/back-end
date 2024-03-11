package com.sideProject.PlanIT.domain.program.entity;

import com.sideProject.PlanIT.common.baseentity.BaseEntity;
import com.sideProject.PlanIT.domain.product.entity.Product;
import com.sideProject.PlanIT.domain.program.entity.ENUM.ProgramStatus;
import com.sideProject.PlanIT.domain.user.entity.Employee;
import com.sideProject.PlanIT.domain.user.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Program extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long Id;

    @ColumnDefault("0")
    private int remainedNumber;

    @Column
    private LocalDate startAt;

    @Column(nullable = true)
    private LocalDate endAt;

    @Enumerated(EnumType.STRING)
    ProgramStatus status;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToOne
    @JoinColumn(name = "registration_id")
    private Registration registration;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Builder
    public Program(int remainedNumber, LocalDate startAt, LocalDate endAt, ProgramStatus status, Product product, Registration registration, Member member, Employee employee) {
        this.remainedNumber = remainedNumber;
        this.startAt = startAt;
        this.endAt = endAt;
        this.status = status;
        this.product = product;
        this.registration = registration;
        this.member = member;
        this.employee = employee;
    }

    //프로그램 변경
    public void updateProgram(LocalDate startAt, LocalDate endAt, Member member, Employee employee) {
        this.startAt = startAt;
        this.endAt = endAt;
        this.member = member;
        this.employee = employee;
    }

    //프로그램 시작 생태로 변경
    public void changeToInProgress() {
        this.status = ProgramStatus.IN_PROGRESS;
    }

    //프로그램 만료
    public void changeToExpired() {
        this.status = ProgramStatus.EXPIRED;
    }

    //프로그램 환불
    public void changeToRefund() {
        this.status = ProgramStatus.REFUND;
    }
}

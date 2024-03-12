package com.sideProject.PlanIT.domain.program.dto.response;

import com.sideProject.PlanIT.domain.product.entity.Product;
import com.sideProject.PlanIT.domain.program.entity.ENUM.ProgramSearchStatus;
import com.sideProject.PlanIT.domain.program.entity.ENUM.ProgramStatus;
import com.sideProject.PlanIT.domain.program.entity.Program;
import com.sideProject.PlanIT.domain.user.entity.Employee;
import com.sideProject.PlanIT.domain.user.entity.Member;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ProgramResponse {
    long id;
    String productName;
    int remainedNumber;
    String startAt;
    String endAt;
    ProgramStatus status;
    String member;
    String employee;

    @Builder
    public ProgramResponse(
            long id,
            int remainedNumber,
            String startAt,
            String endAt,
            ProgramStatus status,
            Product product,
            Member member,
            Employee employee) {
        this.id = id;
        this.remainedNumber = remainedNumber;
        this.startAt = startAt;
        this.endAt = endAt;
        this.status = status;
        this.productName = product.getName();
        this.member = member.getName();
        this.employee = employee.getMember().getName();
    }

    public static ProgramResponse of(Program program){
        return ProgramResponse.builder()
                .id(program.getId())
                .remainedNumber(program.getRemainedNumber())
                .startAt(program.getStartAt().toString())
                .endAt(program.getEndAt().toString())
                .status(program.getStatus())
                .product(program.getProduct())
                .member(program.getMember())
                .employee(program.getEmployee())
                .build();
    }
}

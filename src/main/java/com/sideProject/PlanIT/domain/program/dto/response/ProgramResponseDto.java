package com.sideProject.PlanIT.domain.program.dto.response;

import com.sideProject.PlanIT.domain.product.entity.Product;
import com.sideProject.PlanIT.domain.product.entity.enums.ProductType;
import com.sideProject.PlanIT.domain.program.entity.enums.ProgramStatus;
import com.sideProject.PlanIT.domain.program.entity.Program;
import com.sideProject.PlanIT.domain.user.dto.member.response.EmployeeSemiResponseDto;
import com.sideProject.PlanIT.domain.user.dto.member.response.MemberSemiResponseDto;
import com.sideProject.PlanIT.domain.user.entity.Employee;
import com.sideProject.PlanIT.domain.user.entity.Member;
import lombok.Builder;
import lombok.Getter;

import java.util.Optional;

@Getter
public class ProgramResponseDto {
    long id;
    String productName;
    ProductType type;
    int remainedNumber;
    String startAt;
    String endAt;
    String suspendAt;
    String resumeAt;
    ProgramStatus status;
    MemberSemiResponseDto member;
    EmployeeSemiResponseDto employee;

    @Builder
    public ProgramResponseDto(
            long id,
            int remainedNumber,
            String startAt,
            String endAt,
            ProgramStatus status,
            Product product,
            Member member,
            Employee employee,
            String suspendAt,
            String resumeAt) {
        this.id = id;
        this.remainedNumber = remainedNumber;
        this.startAt = startAt;
        this.endAt = endAt;
        this.status = status;
        this.productName = product.getName();
        this.type = product.getType();
        this.member = MemberSemiResponseDto.of(member);
        this.employee = EmployeeSemiResponseDto.of(employee);
        this.suspendAt = suspendAt;
        this.resumeAt = resumeAt;
    }

    public static ProgramResponseDto of(Program program){
        return ProgramResponseDto.builder()
                .id(program.getId())
                .remainedNumber(program.getRemainedNumber())
                .status(program.getStatus())
                .product(program.getProduct())
                .member(program.getMember())
                .employee(program.getEmployee())
                .startAt(program.getStartAt().toString())
                .endAt(Optional.ofNullable(program.getEndAt())
                        .map(Object::toString)
                        .orElse(null))
                .suspendAt(Optional.ofNullable(program.getSuspendAt())
                        .map(Object::toString)
                        .orElse(null))
                .resumeAt(Optional.ofNullable(program.getResumeAt())
                        .map(Object::toString)
                        .orElse(null))
                .build();
    }
}

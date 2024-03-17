package com.sideProject.PlanIT.domain.user.dto.member.response;

import com.sideProject.PlanIT.domain.user.entity.Employee;
import lombok.Builder;
import lombok.Getter;

@Getter
public class EmployeeSemiResponseDto {
    private Long id;
    private String name;

    @Builder
    public EmployeeSemiResponseDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static EmployeeSemiResponseDto of(Employee employee) {
        if(employee == null) {
            return null;
        }

        return EmployeeSemiResponseDto.builder()
                .id(employee.getId())
                .name(employee.getMember().getName())
                .build();
    }
}

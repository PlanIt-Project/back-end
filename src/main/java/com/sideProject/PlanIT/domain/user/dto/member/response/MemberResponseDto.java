package com.sideProject.PlanIT.domain.user.dto.member.response;

import com.sideProject.PlanIT.domain.user.dto.employee.response.TrainerResponse;
import com.sideProject.PlanIT.domain.user.dto.employee.response.TrainerResponseDto;
import com.sideProject.PlanIT.domain.user.entity.ENUM.MemberRole;
import com.sideProject.PlanIT.domain.user.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberResponseDto {
    private Long Id;
    private String email;
    private String name;
    private String phone_number;
    private LocalDate birth;
    private String address;
    private MemberRole role;
    private TrainerResponse trainerInfo;

    @Builder
    public MemberResponseDto(Long Id, String email, String name, String phone_number, LocalDate birth, String address, MemberRole role, TrainerResponse trainerInfo) {
        this.email = email;
        this.name = name;
        this.phone_number = phone_number;
        this.birth = birth;
        this.address = address;
        this.role = role;
        this.trainerInfo = trainerInfo;
    }

    public static MemberResponseDto of(Member member, TrainerResponse trainerResponse) {
        return MemberResponseDto.builder()
                .email(member.getEmail())
                .name(member.getName())
                .phone_number(member.getPhone_number())
                .birth(member.getBirth())
                .address(member.getAddress())
                .role(member.getRole())
                .trainerInfo(trainerResponse)
                .build();
    }
}

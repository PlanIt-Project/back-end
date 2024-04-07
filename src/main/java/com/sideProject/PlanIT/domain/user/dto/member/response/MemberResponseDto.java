package com.sideProject.PlanIT.domain.user.dto.member.response;

import com.sideProject.PlanIT.domain.user.dto.employee.response.TrainerSubResponseDto;
import com.sideProject.PlanIT.domain.user.entity.enums.Gender;
import com.sideProject.PlanIT.domain.user.entity.enums.MemberRole;
import com.sideProject.PlanIT.domain.user.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberResponseDto {
    private Long id;
    private String email;
    private String name;
    private String phone_number;
    private LocalDate birth;
    private String address;
    private String gender;
    private MemberRole role;
    private TrainerSubResponseDto trainerInfo;

    @Builder
    public MemberResponseDto(
            Long id,
            String email,
            String name,
            String phone_number,
            LocalDate birth,
            String address,
            MemberRole role,
            TrainerSubResponseDto trainerInfo,
            Gender gender
    ) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.phone_number = phone_number;
        this.birth = birth;
        this.address = address;
        this.role = role;
        this.trainerInfo = trainerInfo;
    }

    public static MemberResponseDto of(Member member, TrainerSubResponseDto trainerSubResponseDto) {
        return MemberResponseDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .phone_number(member.getPhone_number())
                .birth(member.getBirth())
                .address(member.getAddress())
                .role(member.getRole())
                .gender(member.getGender())
                .trainerInfo(trainerSubResponseDto)
                .build();
    }
}

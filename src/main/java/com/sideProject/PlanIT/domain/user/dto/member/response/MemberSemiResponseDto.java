package com.sideProject.PlanIT.domain.user.dto.member.response;

import com.sideProject.PlanIT.domain.user.entity.Member;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class MemberSemiResponseDto {
    private Long id;
    private String name;

    @Builder
    public MemberSemiResponseDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static MemberSemiResponseDto of(Member member) {
        return MemberSemiResponseDto.builder()
                .id(member.getId())
                .name(member.getName())
                .build();
    }
}

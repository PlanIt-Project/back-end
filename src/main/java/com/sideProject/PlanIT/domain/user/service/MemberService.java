package com.sideProject.PlanIT.domain.user.service;

import com.sideProject.PlanIT.domain.user.dto.MemberDto;
import com.sideProject.PlanIT.domain.user.entity.Member;

import java.util.List;

public interface MemberService {
    Member signUp(MemberDto.MemberSignUpRequestDto memberSignUpRequestDto);
    String deleteMember(Long member_id);
    Member editMember(Long member_id, MemberDto.MemberEditRequestDto memberEditRequestDto);
    String changePassword(Long member_id, MemberDto.MemberChangePasswordRequestDto memberChangePasswordRequestDto);
    MemberDto.MemberResponseDto findMember(Long member_id);

    List<MemberDto.MemberResponseDto> findAllMembers();
    List<MemberDto.MemberResponseDto> findAllEmployees();
    String grantEmployeeAuth(Long member_id);
}

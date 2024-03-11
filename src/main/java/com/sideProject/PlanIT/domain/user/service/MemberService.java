package com.sideProject.PlanIT.domain.user.service;

import com.sideProject.PlanIT.domain.user.dto.employee.request.TrainerRequestDto;
import com.sideProject.PlanIT.domain.user.dto.employee.response.TrainerResponseDto;
import com.sideProject.PlanIT.domain.user.dto.member.request.MemberChangePasswordRequestDto;
import com.sideProject.PlanIT.domain.user.dto.member.request.MemberEditRequestDto;
import com.sideProject.PlanIT.domain.user.dto.member.request.MemberSignUpRequestDto;
import com.sideProject.PlanIT.domain.user.dto.member.response.MemberResponseDto;
import com.sideProject.PlanIT.domain.user.entity.Member;

import java.util.List;

public interface MemberService {
    Member signUp(MemberSignUpRequestDto memberSignUpRequestDto);
    String deleteMember(Long member_id);
    Member editMember(Long member_id, MemberEditRequestDto memberEditRequestDto);
    String changePassword(Long member_id, MemberChangePasswordRequestDto memberChangePasswordRequestDto);
    MemberResponseDto findMember(Long member_id);

    List<MemberResponseDto> findAllMembers();
    List<TrainerResponseDto> findAllEmployees();
    String grantEmployeeAuth(Long member_id, TrainerRequestDto trainerRequestDto);
}

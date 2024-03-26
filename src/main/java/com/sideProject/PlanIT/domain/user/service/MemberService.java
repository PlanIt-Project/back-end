package com.sideProject.PlanIT.domain.user.service;

import com.sideProject.PlanIT.domain.user.controller.enums.MemberSearchOption;
import com.sideProject.PlanIT.domain.user.dto.employee.request.TrainerRequestDto;
import com.sideProject.PlanIT.domain.user.dto.employee.response.TrainerResponseDto;
import com.sideProject.PlanIT.domain.user.dto.member.request.MemberChangePasswordRequestDto;
import com.sideProject.PlanIT.domain.user.dto.member.request.MemberEditRequestDto;
import com.sideProject.PlanIT.domain.user.dto.member.request.MemberSignInRequestDto;
import com.sideProject.PlanIT.domain.user.dto.member.request.MemberSignUpRequestDto;
import com.sideProject.PlanIT.domain.user.dto.member.response.JwtResponseDto;
import com.sideProject.PlanIT.domain.user.dto.member.response.MemberResponseDto;
import com.sideProject.PlanIT.domain.user.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberService {
    Member signUp(MemberSignUpRequestDto memberSignUpRequestDto);
    JwtResponseDto memberValidation(MemberSignInRequestDto memberSignInRequestDto);
    String deleteMember(Long member_id);

    String editMember(Long member_id, MemberEditRequestDto memberEditRequestDto);
    String changePassword(Long member_id, MemberChangePasswordRequestDto memberChangePasswordRequestDto);
    MemberResponseDto findMember(Long member_id);
    String signOut(Long member_id);
    Page<MemberResponseDto> find(MemberSearchOption option, Pageable pageable);
    Page<TrainerResponseDto> findAllEmployees(Pageable pageable);
    String grantEmployeeAuth(Long member_id, TrainerRequestDto trainerRequestDto);
}

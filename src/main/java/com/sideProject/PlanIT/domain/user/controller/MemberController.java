package com.sideProject.PlanIT.domain.user.controller;

import com.sideProject.PlanIT.common.response.ApiResponse;
import com.sideProject.PlanIT.domain.user.dto.employee.request.TrainerRequestDto;
import com.sideProject.PlanIT.domain.user.dto.employee.response.TrainerResponseDto;
import com.sideProject.PlanIT.domain.user.dto.member.request.MemberChangePasswordRequestDto;
import com.sideProject.PlanIT.domain.user.dto.member.request.MemberEditRequestDto;
import com.sideProject.PlanIT.domain.user.dto.member.request.MemberSignUpRequestDto;
import com.sideProject.PlanIT.domain.user.dto.member.response.MemberResponseDto;
import com.sideProject.PlanIT.domain.user.entity.Member;
import com.sideProject.PlanIT.domain.user.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/member/signup")
    public ApiResponse<Member> signUp(@RequestBody MemberSignUpRequestDto memberSignUpRequestDto) {
        return ApiResponse.ok(memberService.signUp(memberSignUpRequestDto));
    }

    @PostMapping("/member/signin")
    public void signIn() {
        //todo: 로그인 기능
    }

    @DeleteMapping("/member/{member_id}")
    public ApiResponse<String> deleteMember(@PathVariable Long member_id) {
        return ApiResponse.ok(memberService.deleteMember(member_id));
    }

    @PutMapping("/member/{member_id}")
    public ApiResponse<Member> editMember(@PathVariable Long member_id, @RequestBody MemberEditRequestDto memberEditRequestDto) {
        return ApiResponse.ok(memberService.editMember(member_id, memberEditRequestDto));
    }

    @PutMapping("/member/password/{member_id}")
    public ApiResponse<String> changePassword(@PathVariable Long member_id, @RequestBody MemberChangePasswordRequestDto memberChangePasswordRequestDto) {
        return ApiResponse.ok(memberService.changePassword(member_id, memberChangePasswordRequestDto));
    }

    @GetMapping("/member/{member_id}")
    public ApiResponse<MemberResponseDto> findMember(@PathVariable Long member_id) {
        return ApiResponse.ok(memberService.findMember(member_id));
    }

    @DeleteMapping("/member/signout")
    public void signout() {
        //todo: 로그아웃 기능
    }

    @GetMapping("/member")
    public ApiResponse<List<MemberResponseDto>> findAllMember() {
        return ApiResponse.ok(memberService.findAllMembers());
    }

    @GetMapping("/member/employee")
    public ApiResponse<List<TrainerResponseDto>> findAllEmployees() {
        return ApiResponse.ok(memberService.findAllEmployees());
    }

    @PutMapping("/member/employee/{member_id}")
    public ApiResponse<String> grantEmployeeAuth(@PathVariable Long member_id, @RequestBody TrainerRequestDto trainerRequestDto) {
        return ApiResponse.ok(memberService.grantEmployeeAuth(member_id, trainerRequestDto));
    }
}

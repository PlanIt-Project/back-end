package com.sideProject.PlanIT.domain.user.controller;

import com.sideProject.PlanIT.common.response.ApiResponse;
import com.sideProject.PlanIT.domain.user.dto.EmployeeDto;
import com.sideProject.PlanIT.domain.user.dto.MemberDto;
import com.sideProject.PlanIT.domain.user.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


//todo: 1. 유저 정보 수정 service 로직 이상함
//todo: 2. api 요청 url 다시 수정
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/member/signup")
    public ApiResponse<?> signUp(@RequestBody MemberDto.MemberSignUpRequestDto memberSignUpRequestDto) {
        return ApiResponse.ok(memberService.signUp(memberSignUpRequestDto));
    }

    @PostMapping("/member/signin")
    public void signIn() {
        //todo: 로그인 기능
    }

    @DeleteMapping("/member/{member_id}")
    public ApiResponse<?> deleteMember(@PathVariable Long member_id) {
        return ApiResponse.ok(memberService.deleteMember(member_id));
    }

    @PutMapping("/member/{member_id}")
    public ApiResponse<?> editMember(@PathVariable Long member_id, @RequestBody MemberDto.MemberEditRequestDto memberEditRequestDto) {
        return ApiResponse.ok(memberService.editMember(member_id, memberEditRequestDto));
    }

    @PutMapping("/member/password/{member_id}")
    public ApiResponse<?> changePassword(@PathVariable Long member_id, @RequestBody MemberDto.MemberChangePasswordRequestDto memberChangePasswordRequestDto) {
        return ApiResponse.ok(memberService.changePassword(member_id, memberChangePasswordRequestDto));
    }

    @GetMapping("/member/{member_id}")
    public ApiResponse<?> findMember(@PathVariable Long member_id) {
        return ApiResponse.ok(memberService.findMember(member_id));
    }

    @DeleteMapping("/member/signout")
    public void signout() {
        //todo: 로그아웃 기능
    }

    @GetMapping("/member")
    public ApiResponse<?> findAllMember() {
        return ApiResponse.ok(memberService.findAllMembers());
    }

    @GetMapping("/member/employee")
    public ApiResponse<?> findAllEmployees() {
        return ApiResponse.ok(memberService.findAllEmployees());
    }

    @PutMapping("/member/employee/{member_id}")
    public ApiResponse<?> grantEmployeeAuth(@PathVariable Long member_id, @RequestBody EmployeeDto.TrainerRequestDto trainerRequestDto) {
        return ApiResponse.ok(memberService.grantEmployeeAuth(member_id, trainerRequestDto));
    }
}

package com.sideProject.PlanIT.domain.user.controller;

import com.sideProject.PlanIT.common.response.ApiResponse;
import com.sideProject.PlanIT.domain.user.dto.employee.request.TrainerRequestDto;
import com.sideProject.PlanIT.domain.user.dto.employee.response.TrainerResponseDto;
import com.sideProject.PlanIT.domain.user.dto.member.request.MemberChangePasswordRequestDto;
import com.sideProject.PlanIT.domain.user.dto.member.request.MemberEditRequestDto;
import com.sideProject.PlanIT.domain.user.dto.member.request.MemberSignInRequestDto;
import com.sideProject.PlanIT.domain.user.dto.member.request.MemberSignUpRequestDto;
import com.sideProject.PlanIT.domain.user.dto.member.response.JwtResponseDto;
import com.sideProject.PlanIT.domain.user.dto.member.response.MemberResponseDto;
import com.sideProject.PlanIT.domain.user.entity.Member;
import com.sideProject.PlanIT.domain.user.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

//todo: 권한으로 api 분기하는 방법이 조건문 말고 있을까?
@RestController
@Slf4j
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/test")
    public String test() {
        return "ok";
    }

    @PostMapping("/member/signup")
    public ApiResponse<Long> signUp(@RequestBody MemberSignUpRequestDto memberSignUpRequestDto) {
        return ApiResponse.ok(memberService.signUp(memberSignUpRequestDto).getId());
    }

    @PostMapping("/member/signin")
    public ApiResponse<JwtResponseDto> signIn(@RequestBody MemberSignInRequestDto memberSignInRequestDto) {
        return ApiResponse.ok(memberService.memberValidation(memberSignInRequestDto));
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

    @GetMapping("/member")
    public ApiResponse<MemberResponseDto> findMember(Principal principal) {
        log.info("test");
        return ApiResponse.ok(memberService.findMember(Long.parseLong(principal.getName())));
    }

    @DeleteMapping("/member/signout")
    public void signout() {
        //todo: 로그아웃 기능
    }

    @GetMapping("admin/member")
    public ApiResponse<List<MemberResponseDto>> findAllMember() {
        return ApiResponse.ok(memberService.findAllMembers());
    }

    @GetMapping("/admin/member/employee")
    public ApiResponse<List<TrainerResponseDto>> findAllEmployees() {
        return ApiResponse.ok(memberService.findAllEmployees());
    }

    @PutMapping("admin/member/employee/{member_id}")
    public ApiResponse<String> grantEmployeeAuth(@PathVariable("member_id") Long member_id, @RequestBody TrainerRequestDto trainerRequestDto) {
        return ApiResponse.ok(memberService.grantEmployeeAuth(member_id, trainerRequestDto));
    }
}

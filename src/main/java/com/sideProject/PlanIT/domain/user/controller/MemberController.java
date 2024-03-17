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
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


//todo: member, admin Controller 분리

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ApiResponse<Long> signUp(@RequestBody MemberSignUpRequestDto memberSignUpRequestDto) {
        return ApiResponse.ok(memberService.signUp(memberSignUpRequestDto).getId());
    }

    @PostMapping("/signin")
    public ApiResponse<JwtResponseDto> signIn(@RequestBody MemberSignInRequestDto memberSignInRequestDto) {
        return ApiResponse.ok(memberService.memberValidation(memberSignInRequestDto));
    }

    @DeleteMapping
    public ApiResponse<String> deleteMember(Principal principal) {
        return ApiResponse.ok(memberService.deleteMember(Long.parseLong(principal.getName())));
    }

    @PutMapping
    public ApiResponse<Member> editMember(Principal principal, @RequestBody MemberEditRequestDto memberEditRequestDto) {
        return ApiResponse.ok(memberService.editMember(Long.parseLong(principal.getName()), memberEditRequestDto));
    }

    @PutMapping("/password")
    public ApiResponse<String> changePassword(Principal principal, @RequestBody MemberChangePasswordRequestDto memberChangePasswordRequestDto) {
        return ApiResponse.ok(memberService.changePassword(Long.parseLong(principal.getName()), memberChangePasswordRequestDto));
    }

    @GetMapping
    public ApiResponse<MemberResponseDto> findMember(Principal principal) {
        log.info("test");
        return ApiResponse.ok(memberService.findMember(Long.parseLong(principal.getName())));
    }

    @DeleteMapping("/signout")
    public ApiResponse<String> signout(Principal principal) {
        return ApiResponse.ok(memberService.signOut(Long.parseLong(principal.getName())));
    }
}

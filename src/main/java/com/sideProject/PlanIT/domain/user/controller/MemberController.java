package com.sideProject.PlanIT.domain.user.controller;

import com.sideProject.PlanIT.common.response.ApiResponse;
import com.sideProject.PlanIT.domain.user.dto.employee.response.TrainerResponseDto;
import com.sideProject.PlanIT.domain.user.dto.member.request.*;
import com.sideProject.PlanIT.domain.user.dto.member.response.JwtResponseDto;
import com.sideProject.PlanIT.domain.user.dto.member.response.MemberResponseDto;
import com.sideProject.PlanIT.domain.user.service.AuthService;
import com.sideProject.PlanIT.domain.user.service.EmailService;
import com.sideProject.PlanIT.domain.user.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final EmailService emailService;
    private final AuthService authService;

    @PostMapping("/signup")
    public ApiResponse<Long> signUp(@RequestBody MemberSignUpRequestDto memberSignUpRequestDto) {
        return ApiResponse.ok(memberService.signUp(memberSignUpRequestDto).getId());
    }

    @PostMapping("/email")
    public ApiResponse<String> mailSend(@RequestBody @Valid EmailSendReqeustDto emailSendReqeustDto) {
        return ApiResponse.ok(emailService.joinEmail(emailSendReqeustDto.getEmail()));
    }

    @PostMapping("/email/check")
    public ApiResponse<String> mailValidation(@RequestBody @Valid EmailValidationRequestDto emailValidationRequestDto) {
        return ApiResponse.ok(emailService.validEmail(emailValidationRequestDto));
    }

    @PostMapping("/signin")
    public ApiResponse<JwtResponseDto> signIn(@RequestBody MemberSignInRequestDto memberSignInRequestDto) {
        return ApiResponse.ok(memberService.memberValidation(memberSignInRequestDto));
    }

    @GetMapping("/refresh")
    public ApiResponse<JwtResponseDto> refreshAccessToken(@RequestHeader("Authorization") String authorizationHeader) {
        return ApiResponse.ok(authService.refreshAccessToken(authorizationHeader));
    }

    @DeleteMapping
    public ApiResponse<String> deleteMember(Principal principal) {
        return ApiResponse.ok(memberService.deleteMember(Long.parseLong(principal.getName())));
    }

    @PutMapping
    public ApiResponse<String> editMember(Principal principal, @RequestBody MemberEditRequestDto memberEditRequestDto) {
        return ApiResponse.ok(memberService.editMember(Long.parseLong(principal.getName()), memberEditRequestDto));
    }

    @PutMapping("/password")
    public ApiResponse<String> changePassword(Principal principal, @RequestBody MemberChangePasswordRequestDto memberChangePasswordRequestDto) {
        return ApiResponse.ok(memberService.changePassword(Long.parseLong(principal.getName()), memberChangePasswordRequestDto));
    }

    @GetMapping
    public ApiResponse<MemberResponseDto> findMember(Principal principal) {
        return ApiResponse.ok(memberService.findMember(Long.parseLong(principal.getName())));
    }

    @DeleteMapping("/signout")
    public ApiResponse<String> signout(Principal principal) {
        return ApiResponse.ok(memberService.signOut(Long.parseLong(principal.getName())));
    }

    @GetMapping("/employee")
    public ApiResponse<Page<TrainerResponseDto>> findAllEmployees(
            @PageableDefault(size = 10) Pageable pageable
    ) {
        return ApiResponse.ok(memberService.findAllEmployees(pageable));
    }
}

package com.sideProject.PlanIT.domain.user.controller;

import com.sideProject.PlanIT.common.response.ApiResponse;
import com.sideProject.PlanIT.domain.user.dto.employee.request.TrainerRequestDto;
import com.sideProject.PlanIT.domain.user.dto.member.response.MemberResponseDto;
import com.sideProject.PlanIT.domain.user.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/member")
public class AdminController {
    private final MemberService memberService;

    @GetMapping("/{member_id}")
    public ApiResponse<MemberResponseDto> findMemberById(@PathVariable(name = "member_id") Long member_id) {
        return ApiResponse.ok(memberService.findMember(member_id));
    }

    @GetMapping
    public ApiResponse<List<MemberResponseDto>> findAllMember() {
        return ApiResponse.ok(memberService.findAllMembers());
    }



    @PutMapping("/employee/{member_id}")
    public ApiResponse<String> grantEmployeeAuth(@PathVariable(name = "member_id") Long member_id, @RequestBody TrainerRequestDto trainerRequestDto) {
        return ApiResponse.ok(memberService.grantEmployeeAuth(member_id, trainerRequestDto));
    }
}

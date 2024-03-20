package com.sideProject.PlanIT.domain.user.controller;

import com.sideProject.PlanIT.common.response.ApiResponse;
import com.sideProject.PlanIT.domain.user.controller.enums.MemberSearchOption;
import com.sideProject.PlanIT.domain.user.dto.employee.request.TrainerRequestDto;
import com.sideProject.PlanIT.domain.user.dto.member.response.MemberResponseDto;
import com.sideProject.PlanIT.domain.user.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

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
    public ApiResponse<Page<MemberResponseDto>> findAllMember(
            @RequestParam(value = "option", required = false, defaultValue = "ALL") MemberSearchOption option,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        return ApiResponse.ok(memberService.find(option,pageable));
    }



    @PutMapping("/employee/{member_id}")
    public ApiResponse<String> grantEmployeeAuth(@PathVariable(name = "member_id") Long member_id, @RequestBody TrainerRequestDto trainerRequestDto) {
        return ApiResponse.ok(memberService.grantEmployeeAuth(member_id, trainerRequestDto));
    }
}

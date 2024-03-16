package com.sideProject.PlanIT.domain.program.controller;

import com.sideProject.PlanIT.common.response.ApiResponse;
import com.sideProject.PlanIT.domain.program.dto.request.ProgramModifyRequest;
import com.sideProject.PlanIT.domain.program.dto.response.ProgramResponse;
import com.sideProject.PlanIT.domain.program.dto.response.RegistrationResponse;
import com.sideProject.PlanIT.domain.program.entity.ENUM.ProgramSearchStatus;
import com.sideProject.PlanIT.domain.program.entity.ENUM.RegistrationSearchStatus;
import com.sideProject.PlanIT.domain.program.service.ProgramService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/program")
public class ProgramAdminController {

    private final ProgramService programService;

    //어드민이 전부 검색
    @GetMapping("")
    public ApiResponse<List<ProgramResponse>> find(@RequestParam(value = "option", required = false, defaultValue = "VALID") ProgramSearchStatus option, Principal principal) {
        //todo : spring security 개발 후 토큰에서 userID를 전달해 줘야함.
        Long id = Long.parseLong(principal.getName());

        return ApiResponse.ok(
                programService.find(id,option)
        );
    }

    //어드민이 유저 id로 검색
    @GetMapping("/{id}")
    public ApiResponse<List<ProgramResponse>> find(
            @PathVariable("id") Long id,
            @RequestParam(value = "option", required = false, defaultValue = "READY") ProgramSearchStatus option) {
        //todo : spring security 개발 후 토큰에서 userID를 전달해 줘야함.
        return ApiResponse.ok(
                programService.findByUser(id, option)
        );
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> refund(@PathVariable("id") Long id) {
        LocalDateTime now = LocalDateTime.now();
        return ApiResponse.ok(
                programService.refund(id,now)
        );
    }

    @PutMapping("/{id}")
    public ApiResponse<String> modify(
            @PathVariable("id") Long id,
            ProgramModifyRequest request) {
        return ApiResponse.ok(
                programService.modify(id, request)
        );
    }

    @PostMapping("/approve/{id}")
    public ApiResponse<Long> approve(@PathVariable("id") Long id, @RequestBody long trainer) {
        LocalDateTime now = LocalDateTime.now();
        return ApiResponse.ok(
                programService.approve(id, trainer, now)
        );
    }

    @GetMapping("/registration")
    public ApiResponse<List<RegistrationResponse>> findRegistration(@RequestParam(value = "option", required = false, defaultValue = "IN_PROGRESS") RegistrationSearchStatus option, Principal principal) {
        Long id = Long.parseLong(principal.getName());
        return ApiResponse.ok(
                programService.findRegistrations(id, option)
        );
    }


    @GetMapping("/registration/{id}")
    public ApiResponse<List<RegistrationResponse>> findRegistrationByUser(
            @PathVariable("id") Long id,
            @RequestParam(value = "option", required = false, defaultValue = "IN_PROGRESS") RegistrationSearchStatus option) {
        //todo : spring security 개발 후 토큰에서 userID를 전달해 줘야함.
        return ApiResponse.ok(
                programService.findRegistrationsByUser(id,option)
        );
    }
}
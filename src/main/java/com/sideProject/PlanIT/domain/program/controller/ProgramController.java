package com.sideProject.PlanIT.domain.program.controller;

import com.sideProject.PlanIT.common.response.ApiResponse;
import com.sideProject.PlanIT.domain.program.dto.request.RegistrationRequest;
import com.sideProject.PlanIT.domain.program.dto.response.ProgramResponse;
import com.sideProject.PlanIT.domain.program.dto.response.FindRegistrationResponse;
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
@RequestMapping("/program")
public class ProgramController {

    private final ProgramService programService;

    @PostMapping("/registration")
    public ApiResponse<?> registration(@RequestBody RegistrationRequest request, Principal principal){
        LocalDateTime now = LocalDateTime.now();
        Long id = Long.parseLong(principal.getName());
        return ApiResponse.ok(programService.registration(request, id, now));
    }

    @GetMapping
    public ApiResponse<List<ProgramResponse>> find(@RequestParam(value = "option", required = false, defaultValue = "VALID") ProgramSearchStatus option, Principal principal) {
        Long id = Long.parseLong(principal.getName());
        return ApiResponse.ok(
                programService.findByUser(id, option)
        );
    }


    //어드민이 유저 id로 검색
    @GetMapping("/{id}")
    public ApiResponse<ProgramResponse> find(
            @PathVariable("id") Long id,
            Principal principal) {
        //todo : spring security 개발 후 토큰에서 userID를 전달해 줘야함.
        Long userId = Long.parseLong(principal.getName());
        return ApiResponse.ok(
                programService.findByProgramId(id, userId)
        );
    }

    @GetMapping("/registration")
    public ApiResponse<List<FindRegistrationResponse>> findRegistration(
            @RequestParam(value = "option", required = false, defaultValue = "ALL") RegistrationSearchStatus option,
            Principal principal) {
        Long id = Long.parseLong(principal.getName());

        return ApiResponse.ok(
                programService.findRegistrationsByUser(id,option)
        );
    }



}

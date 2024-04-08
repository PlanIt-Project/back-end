package com.sideProject.PlanIT.domain.program.controller;

import com.sideProject.PlanIT.common.response.ApiResponse;
import com.sideProject.PlanIT.domain.program.dto.request.RegistrationRequestDto;
import com.sideProject.PlanIT.domain.program.dto.response.FindRegistrationResponseDto;
import com.sideProject.PlanIT.domain.program.dto.response.ProgramResponseDto;
import com.sideProject.PlanIT.domain.program.entity.enums.ProgramSearchStatus;
import com.sideProject.PlanIT.domain.program.entity.enums.RegistrationSearchStatus;
import com.sideProject.PlanIT.domain.program.service.ProgramService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/program")
public class ProgramController {

    private final ProgramService programService;

    @PostMapping("/registration")
    public ApiResponse<?> registration(@Valid @RequestBody RegistrationRequestDto request, Principal principal){
        LocalDateTime now = LocalDateTime.now();
        Long id = Long.parseLong(principal.getName());
        return ApiResponse.ok(programService.registration(request, id, now));
    }

    @GetMapping
    public ApiResponse<Page<ProgramResponseDto>> find(
            @RequestParam(value = "option", required = false, defaultValue = "VALID") ProgramSearchStatus option,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            Principal principal) {
        long id = Long.parseLong(principal.getName());
        return ApiResponse.ok(
                programService.findByUser(id, option,pageable)
        );
    }

    @GetMapping("/{id}")
    public ApiResponse<ProgramResponseDto> findById(
            @PathVariable("id") Long id,
            Principal principal) {
        long userId = Long.parseLong(principal.getName());
        return ApiResponse.ok(
                programService.findByProgramId(id, userId)
        );
    }

    @GetMapping("/registration")
    public ApiResponse<Page<FindRegistrationResponseDto>> findRegistration(
            @RequestParam(value = "option", required = false, defaultValue = "ALL") RegistrationSearchStatus option,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            Principal principal) {
        long id = Long.parseLong(principal.getName());

        return ApiResponse.ok(
                programService.findRegistrationsByUser(id,option, pageable)
        );
    }



}

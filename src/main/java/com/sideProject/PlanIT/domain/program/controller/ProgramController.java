package com.sideProject.PlanIT.domain.program.controller;

import com.sideProject.PlanIT.common.response.ApiResponse;
import com.sideProject.PlanIT.common.response.ErrorCode;
import com.sideProject.PlanIT.domain.program.dto.request.ProgramModifyRequest;
import com.sideProject.PlanIT.domain.program.dto.request.ProgramRegistraion;
import com.sideProject.PlanIT.domain.program.dto.response.ProgramResponse;
import com.sideProject.PlanIT.domain.program.dto.response.RegistrationResponse;
import com.sideProject.PlanIT.domain.program.entity.ENUM.ProgramSearchStatus;
import com.sideProject.PlanIT.domain.program.entity.ENUM.RegistrationSearchStatus;
import com.sideProject.PlanIT.domain.program.service.ProgramService;
import jakarta.websocket.server.PathParam;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import static com.sideProject.PlanIT.domain.program.entity.ENUM.ProgramSearchStatus.INVALID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/program")
public class ProgramController {

    private final ProgramService programService;

    @PostMapping("/registration")
    public ApiResponse<?> registration(@RequestBody ProgramRegistraion.programRegistrationrequest programRegistrationrequest){
        try {
            return ApiResponse.ok(programService.registration(programRegistrationrequest));
        } catch (Exception e){
            return ApiResponse.error(ErrorCode.INVALID_PARAMETER);
        }
    }

    @GetMapping
    public ApiResponse<List<ProgramResponse>> find(@RequestParam(value = "option", required = false, defaultValue = "VALID") ProgramSearchStatus option, Principal principal) {
        Long id = Long.parseLong(principal.getName());
        return ApiResponse.ok(
                programService.findByUser(id, option)
        );
    }

    @GetMapping("/registration")
    public ApiResponse<List<RegistrationResponse>> findRegistration(
            @RequestParam(value = "option", required = false, defaultValue = "ALL") RegistrationSearchStatus option,
            Principal principal) {
        Long id = Long.parseLong(principal.getName());

        return ApiResponse.ok(
                programService.findRegistrationsByUser(id,option)
        );
    }



}

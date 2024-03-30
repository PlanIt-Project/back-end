package com.sideProject.PlanIT.domain.user.controller;

import com.sideProject.PlanIT.common.response.ApiResponse;
import com.sideProject.PlanIT.domain.user.dto.employee.response.TrainerScheduleResponseDto;
import com.sideProject.PlanIT.domain.user.service.WorktimeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import com.sideProject.PlanIT.domain.user.dto.employee.request.TrainerScheduleRequestDto;

import java.security.Principal;
import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/reservation")

public class TrainerScheduleController {
    private final WorktimeService worktimeservice;
    @PostMapping("/trainer")
    public ApiResponse<?> trainerScheduleRegistration(@RequestBody List<TrainerScheduleRequestDto> request, Principal principal) {
        Long id = Long.parseLong(principal.getName());
        return ApiResponse.ok(worktimeservice.trainerScheduleRegistration(request,id));
    }




    //특정 직원 일정 조회(출퇴근)
//    @GetMapping("/trainer/{trainer_Id}")
//    public String findtrainerSchedule(){
//
//        return "";
//    }
}

package com.sideProject.PlanIT.domain.user.controller;

import com.sideProject.PlanIT.common.response.ApiResponse;
import com.sideProject.PlanIT.domain.user.dto.employee.request.TrainerSchduleChageRequestDto;
import com.sideProject.PlanIT.domain.user.dto.employee.response.TrainerScheduleResponseDto;
import com.sideProject.PlanIT.domain.user.service.WorktimeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminTrainerScheduleController {
    private final WorktimeService worktimeservice;
    // 직원 출퇴근 확인
    @GetMapping("/trainerschedule")
    public ApiResponse<Page<TrainerScheduleResponseDto>> findalltrainerSchedule(@PageableDefault(size = 10) Pageable pageable, Principal principal){
        Long id = Long.parseLong(principal.getName());
        return ApiResponse.ok(worktimeservice.FindTrainerSchedule(id,pageable));
    }

    // 특정 직원 일정 수정(출퇴근)
    @PutMapping("/trainerschedule/{schedule_id}")
    public ApiResponse<String> updatetrainerSchedule(@PathVariable("schedule_id") Long id, TrainerSchduleChageRequestDto request){

        return ApiResponse.ok(worktimeservice.TrainerScheduleChange(request,id));
    }
}

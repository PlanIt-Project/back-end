package com.sideProject.PlanIT.domain.user.controller;

import com.sideProject.PlanIT.common.response.ApiResponse;
import com.sideProject.PlanIT.domain.user.dto.employee.request.TrainerScheduleChangeRequestDto;
import com.sideProject.PlanIT.domain.user.dto.employee.response.TrainerScheduleResponseDto;
import com.sideProject.PlanIT.domain.user.service.WorktimeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminTrainerScheduleController {
    private final WorktimeService worktimeservice;
    // 직원 출퇴근 확인
    @GetMapping("/trainerschedule")
    public ApiResponse<Page<TrainerScheduleResponseDto>> findallTrainerSchedule(@PageableDefault(size = 10) Pageable pageable, Principal principal){
        Long id = Long.parseLong(principal.getName());
        return ApiResponse.ok(worktimeservice.findTrainerSchedule(id,pageable));
    }

    // 특정 직원 일정 수정(출퇴근)
    @PutMapping("/trainerschedule/{schedule_id}")
    public ApiResponse<String> updateTrainerSchedule(@PathVariable("schedule_id") Long id, @RequestBody TrainerScheduleChangeRequestDto request){

        return ApiResponse.ok(worktimeservice.trainerScheduleChange(request,id));
    }
    // 특정 직원 일정 조회
    @GetMapping("/trainerschedule/{trainer_id}")
    public ApiResponse<List<TrainerScheduleResponseDto>> findTrainerSchedule(@PathVariable("trainer_id") Long trainer_id, Principal principal, Pageable pageable){
        Long id = Long.parseLong(principal.getName());
        return ApiResponse.ok(worktimeservice.findOneTrainerSchedule(trainer_id,id));
    };
}

package com.sideProject.PlanIT.domain.user.controller;

import com.sideProject.PlanIT.common.response.ApiResponse;
import com.sideProject.PlanIT.domain.user.dto.employee.response.TrainerScheduleResponseDto;
import com.sideProject.PlanIT.domain.user.dto.member.response.MemberResponseDto;
import com.sideProject.PlanIT.domain.user.service.WorktimeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import com.sideProject.PlanIT.domain.user.dto.employee.request.TrainerScheduleRequestDto;

import java.security.Principal;
import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")

public class TrainerScheduleController {
    private final WorktimeService worktimeservice;
    @PostMapping("/trainerschedule")
    public ApiResponse<?> trainerScheduleRegistration(@RequestBody List<TrainerScheduleRequestDto> request, Principal principal) {
        Long id = Long.parseLong(principal.getName());
        return ApiResponse.ok(worktimeservice.trainerScheduleRegistration(request,id));
    }

    @GetMapping("/trainerschedule/{trainer_id}")
    public ApiResponse<List<TrainerScheduleResponseDto>> findTrainerSchedule(@PathVariable("trainer_id") Long trainer_id, Principal principal, Pageable pageable){
        Long id = Long.parseLong(principal.getName());
        return ApiResponse.ok(worktimeservice.findoneTrianerSchedule(trainer_id,id));
    };



    //특정 직원 일정 조회(출퇴근)
//    @GetMapping("/trainer/{trainer_Id}")
//    public String findtrainerSchedule(){
//
//        return "";
//    }
}

package com.sideProject.PlanIT.domain.user.service;

import com.sideProject.PlanIT.domain.user.dto.employee.request.TrainerSchduleChageRequestDto;
import com.sideProject.PlanIT.domain.user.dto.employee.request.TrainerScheduleRequestDto;
import com.sideProject.PlanIT.domain.user.dto.employee.response.TrainerScheduleRegistrationResponse;
import com.sideProject.PlanIT.domain.user.dto.employee.response.TrainerScheduleResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WorktimeService {
    // 트레이너 출퇴근 등록
    public TrainerScheduleRegistrationResponse TrainerScheduleRegistration(TrainerScheduleRequestDto request, Long id);
    // 트레이너 출퇴근 수정
    public String TrainerScheduleChange(TrainerSchduleChageRequestDto request, Long schedule_id);
    // 트레이너 출퇴근 전체 조회
    public Page<TrainerScheduleResponseDto> FindTrainerSchedule(Long id, Pageable pageable);
    // 특정 트레이너 출퇴근 조회

}

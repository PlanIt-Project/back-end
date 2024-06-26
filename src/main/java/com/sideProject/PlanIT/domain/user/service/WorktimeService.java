package com.sideProject.PlanIT.domain.user.service;

import com.sideProject.PlanIT.domain.user.dto.employee.request.TrainerScheduleChangeRequestDto;
import com.sideProject.PlanIT.domain.user.dto.employee.request.TrainerScheduleRequestDto;
import com.sideProject.PlanIT.domain.user.dto.employee.response.TrainerScheduleRegistrationResponseDto;
import com.sideProject.PlanIT.domain.user.dto.employee.response.TrainerScheduleResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface WorktimeService {
    // 트레이너 출퇴근 등록
    public TrainerScheduleRegistrationResponseDto trainerScheduleRegistration(List<TrainerScheduleRequestDto> request, Long id);
    // 트레이너 출퇴근 수정
    public String trainerScheduleChange(TrainerScheduleChangeRequestDto request, Long schedule_id);
    // 트레이너 출퇴근 전체 조회
    public Page<TrainerScheduleResponseDto> findTrainerSchedule(Long id, Pageable pageable);
    // 특정 트레이너 출퇴근 조회
    public List<TrainerScheduleResponseDto> findOneTrainerSchedule(Long trainer_id, Long id);
}

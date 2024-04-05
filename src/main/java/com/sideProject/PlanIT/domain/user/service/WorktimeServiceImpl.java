package com.sideProject.PlanIT.domain.user.service;

import com.sideProject.PlanIT.common.response.CustomException;
import com.sideProject.PlanIT.common.response.ErrorCode;

import com.sideProject.PlanIT.domain.user.dto.employee.request.TrainerSchduleChangeRequestDto;
import com.sideProject.PlanIT.domain.user.dto.employee.request.TrainerScheduleRequestDto;
import com.sideProject.PlanIT.domain.user.dto.employee.response.TrainerScheduleRegistrationResponseDto;
import com.sideProject.PlanIT.domain.user.dto.employee.response.TrainerScheduleResponseDto;
import com.sideProject.PlanIT.domain.user.entity.Employee;
import com.sideProject.PlanIT.domain.user.entity.Member;
import com.sideProject.PlanIT.domain.user.entity.WorkTime;
import com.sideProject.PlanIT.domain.user.entity.enums.MemberRole;
import com.sideProject.PlanIT.domain.user.repository.EmployeeRepository;
import com.sideProject.PlanIT.domain.user.repository.MemberRepository;
import com.sideProject.PlanIT.domain.user.repository.WorkTimeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class WorktimeServiceImpl implements WorktimeService {
    // 트레이너 출퇴근 등록
    private final EmployeeRepository employeeRepository;
    private final MemberRepository memberRepository;
    private final WorkTimeRepository worktimeRepository;
    @Override
    public TrainerScheduleRegistrationResponseDto trainerScheduleRegistration(List<TrainerScheduleRequestDto> request, Long id){

        Member member = memberRepository.findById(id).orElseThrow(() -> new CustomException("존재하지 않는 회원입니다.", ErrorCode.MEMBER_NOT_FOUND));
        Employee trainer = employeeRepository.findByMemberId(member.getId()).orElseThrow(() -> new CustomException("존재하지 않는 직원입니다", ErrorCode.EMPLOYEE_NOT_FOUND));


        for (TrainerScheduleRequestDto requestdto : request){
            worktimeRepository.save(WorkTime.builder()
                    .week(requestdto.getWeek())
                    .startAt(requestdto.getStartAt())
                    .endAt(requestdto.getEndAt())
                    .employee(trainer)
                    .build());

        return TrainerScheduleRegistrationResponse.of(trainer.getId(),"출퇴근시간이 등록되었습니다.");
    }

    // 특정 일정 가져오기
    private WorkTime getTrainerSchduleByID(Long schdule_id){
        return worktimeRepository.findById(schdule_id).orElseThrow(() -> new CustomException("일정이 존재하지 않습니다.",ErrorCode.TrainerSchedule_NOT_FOUND));
    }
    // 트레이너 출퇴근 수정
    @Override
    public String trainerScheduleChange(TrainerSchduleChangeRequestDto request, Long schedule_id){
        WorkTime TrainerSchedule = getTrainerSchduleByID(schedule_id);
        TrainerSchedule.ChageWorktime(request.getStartAt(),request.getEndAt());

        worktimeRepository.save(TrainerSchedule);
        return "OK";
    }


    // 트레이너 출퇴근 조회

    @Override
    public Page<TrainerScheduleResponseDto> findTrainerSchedule(Long id, Pageable pageable){
        Member member = memberRepository.findById(id).orElseThrow(()-> new CustomException("존재하지 않는 회원입니다.",ErrorCode.MEMBER_NOT_FOUND));
        if(member.getRole() != MemberRole.ADMIN && member.getRole() != MemberRole.TRAINER){
            throw new CustomException("권한이 없습니다.",ErrorCode.NO_AUTHORITY);
        }
        Page<WorkTime> worktime = worktimeRepository.findAll(pageable);
        return worktime.map(TrainerScheduleResponseDto::of);
    }

    // 특정 트레이너 출퇴근 조회
    @Override
    public List<TrainerScheduleResponseDto> findoneTrianerSchedule(Long employee_id,Long id) {
        Member member = memberRepository.findById(id).orElseThrow(()-> new CustomException("존재하지 않는 회원입니다.",ErrorCode.MEMBER_NOT_FOUND));
        if(member.getRole() != MemberRole.ADMIN && member.getRole() != MemberRole.TRAINER){
            throw new CustomException("권한이 없습니다.",ErrorCode.NO_AUTHORITY);
        }
        List<WorkTime> worktime = worktimeRepository.findByEmployeeId(employee_id);
        if (worktime.isEmpty()){
            throw new CustomException("일정을 찾을 수 없습니다.",ErrorCode.TrainerSchedule_NOT_FOUND);
        }
        return worktime.stream()
                .map(TrainerScheduleResponseDto::of)
                .collect(Collectors.toList());

    }

}

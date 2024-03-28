package com.sideProject.PlanIT.domain.user.service;

import com.sideProject.PlanIT.common.response.CustomException;
import com.sideProject.PlanIT.common.response.ErrorCode;
import com.sideProject.PlanIT.domain.user.dto.employee.request.TrainerSchduleChageRequestDto;
import com.sideProject.PlanIT.domain.user.dto.employee.request.TrainerScheduleRequestDto;
import com.sideProject.PlanIT.domain.user.dto.employee.response.TrainerScheduleRegistrationResponse;
import com.sideProject.PlanIT.domain.user.dto.employee.response.TrainerScheduleResponseDto;
import com.sideProject.PlanIT.domain.user.entity.Employee;
import com.sideProject.PlanIT.domain.user.entity.Member;
import com.sideProject.PlanIT.domain.user.entity.WorkTime;
import com.sideProject.PlanIT.domain.user.entity.enums.MemberRole;
import com.sideProject.PlanIT.domain.user.repository.EmployeeRepository;
import com.sideProject.PlanIT.domain.user.repository.MemberRepository;
import com.sideProject.PlanIT.domain.user.repository.WorktimeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class WorktimeServiceImpl implements WorktimeService {
    // 트레이너 출퇴근 등록
    private final EmployeeRepository employeeRepository;
    private final MemberRepository memberRepository;
    private final WorktimeRepository worktimeRepository;
    @Override
    public TrainerScheduleRegistrationResponse TrainerScheduleRegistration(TrainerScheduleRequestDto request, Long id){
        Member member = memberRepository.findById(id).orElseThrow(() -> new CustomException("존재하지 않는 회원입니다.", ErrorCode.MEMBER_NOT_FOUND));
        Employee trainer = employeeRepository.findByMemberId(member.getId()).orElseThrow(() -> new CustomException("존재하지 않는 직원입니다", ErrorCode.EMPLOYEE_NOT_FOUND));

        for (int i = 0; i<=6;i++){
            worktimeRepository.save(WorkTime.builder().week(request.getWeek().get(i)).startAt(request.getStartAt().get(i)).endAt(request.getEndAt().get(i)).employee(trainer).build()
            );

        }

        return TrainerScheduleRegistrationResponse.of(trainer.getId(),"출퇴근시간이 등록되었습니다.");
    }

    // 특정 트레이너 일정 가져오기
    private WorkTime getTrainerSchduleByID(Long schdule_id){
        return worktimeRepository.findById(schdule_id).orElseThrow(() -> new CustomException("일정이 존재하지 않습니다.",ErrorCode.TrainerSchedule_NOT_FOUND));
    }
    // 트레이너 출퇴근 수정
    public String TrainerScheduleChange(TrainerSchduleChageRequestDto request, Long schedule_id){
        WorkTime TrainerSchdule = getTrainerSchduleByID(schedule_id);
        TrainerSchdule.ChageWorktime(request.getStartAt(),request.getEndAt());

        worktimeRepository.save(TrainerSchdule);
        return "OK";
    }


    // 트레이너 출퇴근 조회

    @Override
    public Page<TrainerScheduleResponseDto> FindTrainerSchedule(Long id, Pageable pageable){
        Member member = memberRepository.findById(id).orElseThrow(()-> new CustomException("존재하지 않는 회원입니다.",ErrorCode.MEMBER_NOT_FOUND));
        if(member.getRole() != MemberRole.ADMIN && member.getRole() != MemberRole.TRAINER){
            throw new CustomException("권한이 없습니다.",ErrorCode.NO_AUTHORITY);
        }
        Page<WorkTime> worktime = worktimeRepository.findAll(pageable);
        return worktime.map(TrainerScheduleResponseDto::of);
    }

        // 특정 트레이너 출퇴근 조회 => 확인 후 진행
}

package com.sideProject.PlanIT.domain.program.service;

import com.sideProject.PlanIT.domain.program.dto.request.ProgramModifyRequestDto;
import com.sideProject.PlanIT.domain.program.dto.request.RegistrationRequestDto;
import com.sideProject.PlanIT.domain.program.dto.response.ProgramResponseDto;
import com.sideProject.PlanIT.domain.program.dto.response.FindRegistrationResponseDto;
import com.sideProject.PlanIT.domain.program.dto.response.RegistrationResponseDto;
import com.sideProject.PlanIT.domain.program.entity.enums.ProgramSearchStatus;

import com.sideProject.PlanIT.domain.program.entity.enums.RegistrationSearchStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.time.LocalDate;
import java.time.LocalDateTime;

public interface ProgramService {
    public RegistrationResponseDto registration(RegistrationRequestDto request, Long memberId, LocalDateTime now);
    public String refund(long programId, LocalDateTime now);
    public String modify(long programId, ProgramModifyRequestDto request);
    public Page<ProgramResponseDto> find(long adminId, ProgramSearchStatus option, Pageable pageable);
    public Page<ProgramResponseDto> findByUser(long userId, ProgramSearchStatus option, Pageable pageable);
    public ProgramResponseDto findByProgramId(long programId, long userId);
    public Long approve(Long programId, Long trainerId,LocalDateTime now);
    public Page<FindRegistrationResponseDto> findRegistrationsByAdmin(long adminId, RegistrationSearchStatus option, Pageable pageable);
    public Page<FindRegistrationResponseDto> findRegistrationsByUser(long userId, RegistrationSearchStatus option, Pageable pageable);
    public Long suspendProgram(Long id, LocalDate now);
    public Long resumeProgram(Long id, LocalDate now);
    public String expiredMemberShipProgram(LocalDate now);
}

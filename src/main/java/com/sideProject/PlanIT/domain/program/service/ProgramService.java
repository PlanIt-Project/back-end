package com.sideProject.PlanIT.domain.program.service;

import com.sideProject.PlanIT.domain.program.dto.request.ProgramModifyRequest;
import com.sideProject.PlanIT.domain.program.dto.request.RegistrationRequest;
import com.sideProject.PlanIT.domain.program.dto.response.ProgramResponse;
import com.sideProject.PlanIT.domain.program.dto.response.FindRegistrationResponse;
import com.sideProject.PlanIT.domain.program.dto.response.RegistrationResponse;
import com.sideProject.PlanIT.domain.program.entity.enums.ProgramSearchStatus;

import com.sideProject.PlanIT.domain.program.entity.enums.RegistrationSearchStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.time.LocalDate;
import java.time.LocalDateTime;

public interface ProgramService {
    public RegistrationResponse registration(RegistrationRequest request, Long memberId, LocalDateTime now);
    public String refund(long programId, LocalDateTime now);
    public String modify(long programId, ProgramModifyRequest request);
    public Page<ProgramResponse> find(long adminId, ProgramSearchStatus option, Pageable pageable);
    public Page<ProgramResponse> findByUser(long userId, ProgramSearchStatus option, Pageable pageable);
    public ProgramResponse findByProgramId(long programId, long userId);
    public Long approve(Long programId, Long trainerId,LocalDateTime now);
    public Page<FindRegistrationResponse> findRegistrations(long adminId, RegistrationSearchStatus option, Pageable pageable);
    public Page<FindRegistrationResponse> findRegistrationsByUser(long userId, RegistrationSearchStatus option, Pageable pageable);
    public Long suspendProgram(Long id, LocalDate now);
    public Long resumeProgram(Long id, LocalDate now);
}

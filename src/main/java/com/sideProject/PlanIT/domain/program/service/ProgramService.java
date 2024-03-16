package com.sideProject.PlanIT.domain.program.service;

import com.sideProject.PlanIT.domain.program.dto.request.ProgramModifyRequest;
import com.sideProject.PlanIT.domain.program.dto.request.ProgramRegistraion;
import com.sideProject.PlanIT.domain.program.dto.request.RegistrationRequest;
import com.sideProject.PlanIT.domain.program.dto.response.ProgramResponse;
import com.sideProject.PlanIT.domain.program.dto.response.RegistrationResponse;
import com.sideProject.PlanIT.domain.program.entity.ENUM.ProgramSearchStatus;

import com.sideProject.PlanIT.domain.program.entity.Registration;

import com.sideProject.PlanIT.domain.program.entity.ENUM.RegistrationSearchStatus;


import java.time.LocalDateTime;
import java.util.List;

public interface ProgramService {
    public String registration(RegistrationRequest request,Long memberId, LocalDateTime now);
    public String refund(long programId, LocalDateTime now);
    public String modify(long programId, ProgramModifyRequest request);
    public List<ProgramResponse> find(long adminId,ProgramSearchStatus option);
    public List<ProgramResponse> findByUser(long userId, ProgramSearchStatus option);
    public Long approve(Long programId, Long trainerId,LocalDateTime now);
    public List<RegistrationResponse> findRegistrations(long adminId, RegistrationSearchStatus option);
    public List<RegistrationResponse> findRegistrationsByUser(long userId, RegistrationSearchStatus option);
}

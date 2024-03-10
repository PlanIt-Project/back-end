package com.sideProject.PlanIT.domain.program.repository;

import com.sideProject.PlanIT.common.response.CustomException;
import com.sideProject.PlanIT.common.response.ErrorCode;
import com.sideProject.PlanIT.domain.program.entity.ENUM.ProgramStatus;
import com.sideProject.PlanIT.domain.program.entity.ENUM.RegistrationStatus;
import com.sideProject.PlanIT.domain.program.entity.Program;
import com.sideProject.PlanIT.domain.program.entity.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

@Repository
public interface ProgramRepository extends JpaRepository<Program, Long> {
    @Query("SELECT o FROM Program o WHERE o.registration.id = :registrationId")
    Program findSingleProgramByRegistrationId(@Param("registrationId") Long registrationId);
    //status가 IN_PROGRESS인 모든 프로그램 조회
    List<Program> findByMemberId(Long memberId);
    List<Program> findByEmployeeId(Long employeeId);

    List<Program> findByMemberIdAndStatus(Long memberId, ProgramStatus status);
    List<Program> findByEmployeeIdAndStatus(Long employeeId,ProgramStatus status);
    List<Program> findByStatus(ProgramStatus status);

}

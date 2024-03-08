package com.sideProject.PlanIT.domain.program.repository;

import com.sideProject.PlanIT.common.response.CustomException;
import com.sideProject.PlanIT.common.response.ErrorCode;
import com.sideProject.PlanIT.domain.program.entity.Program;
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
    @Query("SELECT p FROM Program p WHERE p.status = 'IN_PROGRESS'")
    List<Program> findInProgressProgramsAll();
    List<Program> findByMemberId(Long memberId);
    List<Program> findByEmployeeId(Long employeeId);
    @Query("SELECT p FROM Program p WHERE p.member.id = :memberId AND p.status = 'IN_PROGRESS'")
    List<Program> findInProgressProgramsByMemberId(@Param("memberId") Long memberId);

    @Query("SELECT p FROM Program p WHERE p.employee.id = :employeeId AND p.status = 'IN_PROGRESS'")
    List<Program> findInProgressProgramsByEmployeeId(@Param("employeeId") Long employeeId);
}

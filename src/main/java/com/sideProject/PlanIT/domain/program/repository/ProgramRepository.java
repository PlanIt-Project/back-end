package com.sideProject.PlanIT.domain.program.repository;

import com.sideProject.PlanIT.domain.program.entity.enums.ProgramStatus;
import com.sideProject.PlanIT.domain.program.entity.Program;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgramRepository extends JpaRepository<Program, Long> {
    @Query("SELECT o FROM Program o WHERE o.registration.id = :registrationId")
    Program findSingleProgramByRegistrationId(@Param("registrationId") Long registrationId);
    //status가 IN_PROGRESS인 모든 프로그램 조회
    Page<Program> findByMemberId(Long memberId, Pageable pageable);
    Page<Program> findByEmployeeId(Long employeeId, Pageable pageable);
    Page<Program> findByMemberIdAndStatus(Long memberId, ProgramStatus status, Pageable pageable);
    Page<Program> findByEmployeeIdAndStatus(Long employeeId,ProgramStatus status, Pageable pageable);
    Page<Program> findByStatus(ProgramStatus status, Pageable pageable);

}

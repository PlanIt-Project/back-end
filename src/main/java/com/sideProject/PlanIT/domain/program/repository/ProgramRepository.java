package com.sideProject.PlanIT.domain.program.repository;

import com.sideProject.PlanIT.domain.product.entity.enums.ProductType;
import com.sideProject.PlanIT.domain.program.entity.Program;
import com.sideProject.PlanIT.domain.program.entity.enums.ProgramStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProgramRepository extends JpaRepository<Program, Long> {
    @Query("SELECT o FROM Program o WHERE o.registration.id = :registrationId")
    Program findSingleProgramByRegistrationId(@Param("registrationId") Long registrationId);
    //status가 IN_PROGRESS인 모든 프로그램 조회
    Page<Program> findByMemberId(Long memberId, Pageable pageable);
    Page<Program> findByEmployeeId(Long employeeId, Pageable pageable);
    Page<Program> findByMemberIdAndStatusIn(Long memberId, List<ProgramStatus> status, Pageable pageable);
    Page<Program> findByEmployeeIdAndStatus(Long employeeId,ProgramStatus status, Pageable pageable);
    Page<Program> findByStatusIn(List<ProgramStatus> status, Pageable pageable);
    List<Program> findByStatusIn(List<ProgramStatus> status);
    @Query("SELECT p FROM Program p JOIN p.product pr WHERE p.endAt = ?1 AND pr.type = ?2")
    List<Program> findMembershipProgramsByEndAtAndProductType(LocalDate endAt, ProductType productType);
}

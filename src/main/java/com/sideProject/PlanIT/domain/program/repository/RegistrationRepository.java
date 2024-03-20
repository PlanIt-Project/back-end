package com.sideProject.PlanIT.domain.program.repository;

import com.sideProject.PlanIT.domain.program.entity.enums.RegistrationStatus;
import com.sideProject.PlanIT.domain.program.entity.Registration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    List<Registration> findByMemberId(Long memberId);
    Page<Registration> findByMemberId(Long memberId, Pageable pageable);
    List<Registration> findByMemberIdAndStatus(Long memberId, RegistrationStatus status);
    Page<Registration> findByMemberIdAndStatus(Long memberId, RegistrationStatus status, Pageable pageable);
    List<Registration> findByStatus(RegistrationStatus status);
    Page<Registration> findByStatus(RegistrationStatus status, Pageable pageable);
}

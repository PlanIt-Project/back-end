package com.sideProject.PlanIT.domain.program.repository;

import com.sideProject.PlanIT.domain.program.entity.ENUM.RegistrationStatus;
import com.sideProject.PlanIT.domain.program.entity.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    List<Registration> findByMemberId(Long memberId);
    List<Registration> findByMemberIdAndStatus(Long memberId, RegistrationStatus status);
    List<Registration> findByStatus(RegistrationStatus status);
}

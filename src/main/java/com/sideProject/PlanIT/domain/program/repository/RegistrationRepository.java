package com.sideProject.PlanIT.domain.program.repository;

import com.sideProject.PlanIT.domain.program.entity.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {
}

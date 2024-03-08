package com.sideProject.PlanIT.domain.user.repository;

import com.sideProject.PlanIT.domain.user.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Long> {
    Optional<Employee> findByMemberId(Long memberId);
}

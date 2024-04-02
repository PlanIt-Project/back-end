package com.sideProject.PlanIT.domain.user.repository;

import com.sideProject.PlanIT.domain.user.entity.WorkTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface WorktimeRepository extends JpaRepository<WorkTime,Long> {
    List<WorkTime> findByEmployeeId(Long employee_id);
}

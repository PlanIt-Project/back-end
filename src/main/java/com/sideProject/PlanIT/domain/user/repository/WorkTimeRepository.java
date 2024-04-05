package com.sideProject.PlanIT.domain.user.repository;

import com.sideProject.PlanIT.domain.user.entity.WorkTime;

import com.sideProject.PlanIT.domain.user.entity.enums.Week;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface WorkTimeRepository extends JpaRepository<WorkTime,Long> {
    List<WorkTime> findByEmployeeId(Long employee_id);
    List<WorkTime> findByEmployeeIdAndWeek(Long employeeId, Week week);
}

package com.sideProject.PlanIT.domain.post.repository;

import com.sideProject.PlanIT.domain.post.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    @Query("SELECT n FROM Notice n WHERE n.startAt <= CURRENT_TIMESTAMP AND n.endAt >= CURRENT_TIMESTAMP ")
    List<Notice> findByStartAtBeforeAndEndAtAfter();
}

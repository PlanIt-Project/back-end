package com.sideProject.PlanIT.domain.post.repository;

import com.sideProject.PlanIT.domain.post.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {
    @Query("SELECT n FROM Notice n WHERE n.startAt <= CURRENT_TIMESTAMP AND n.endAt >= CURRENT_TIMESTAMP ")
    Page<Notice> findByStartAtBeforeAndEndAtAfter(Pageable pageable);

    // for test
    Optional<Notice> findByTitle(String title);
}

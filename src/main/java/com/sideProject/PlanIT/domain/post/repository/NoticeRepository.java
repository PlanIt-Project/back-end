package com.sideProject.PlanIT.domain.post.repository;

import com.sideProject.PlanIT.domain.post.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
}

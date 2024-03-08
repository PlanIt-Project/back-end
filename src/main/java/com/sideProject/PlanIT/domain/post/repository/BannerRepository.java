package com.sideProject.PlanIT.domain.post.repository;

import com.sideProject.PlanIT.domain.post.entity.Banner;
import com.sideProject.PlanIT.domain.post.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BannerRepository extends JpaRepository<Banner, Long> {
    @Query("SELECT b FROM Banner b WHERE b.startAt <= CURRENT_TIMESTAMP AND b.endAt >= CURRENT_TIMESTAMP ")
    List<Banner> findByStartAtBeforeAndEndAtAfter();
}

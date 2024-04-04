package com.sideProject.PlanIT.domain.post.repository;

import com.sideProject.PlanIT.domain.post.entity.Banner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BannerRepository extends JpaRepository<Banner, Long> {
    @Query("SELECT b FROM Banner b WHERE b.startAt <= CURRENT_TIMESTAMP AND b.endAt >= CURRENT_TIMESTAMP ")
    Page<Banner> findByStartAtBeforeAndEndAtAfter(Pageable pageable);
}

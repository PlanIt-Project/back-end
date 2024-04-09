package com.sideProject.PlanIT.domain.post.entity;

import com.sideProject.PlanIT.domain.post.dto.request.BannerRequestDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Banner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column
    private String title;

    @Column
    private LocalDate startAt;

    @Column
    private LocalDate endAt;

    @Column
    private String imagePath;

    @Builder
    public Banner(String title, LocalDate startAt, LocalDate endAt, String imagePath) {
        this.title = title;
        this.startAt = startAt;
        this.endAt = endAt;
        this.imagePath = imagePath;
    }

    public Banner update(BannerRequestDto bannerRequestDto, String imagePath) {
        this.title = bannerRequestDto.getTitle();
        this.startAt = bannerRequestDto.getStartAt();
        this.endAt = bannerRequestDto.getEndAt();
        this.imagePath = imagePath;

        return this;
    }
}

package com.sideProject.PlanIT.domain.post.entity;

import com.sideProject.PlanIT.domain.post.dto.request.BannerRequestDto;
import com.sideProject.PlanIT.domain.post.dto.response.BannerResponseDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Banner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long Id;

    @Column
    private String title;

    @Column
    private LocalDateTime startAt;

    @Column
    private LocalDateTime endAt;

    @Column
    private String imagePath;

    public Banner update(BannerRequestDto bannerRequestDto) {
        this.title = bannerRequestDto.getTitle();
        this.startAt = bannerRequestDto.getStartAt();
        this.endAt = bannerRequestDto.getEndAt();
        if (bannerRequestDto.getImage() != null) {
            this.imagePath = bannerRequestDto.getImage().getOriginalFilename();
        }

        return this;
    }

    public static BannerResponseDto toDto(Banner banner) {
        return BannerResponseDto.builder()
                .title(banner.title)
                .startAt(banner.startAt)
                .endAt(banner.endAt)
                .imagePath(banner.imagePath)
                .build();
    }
}

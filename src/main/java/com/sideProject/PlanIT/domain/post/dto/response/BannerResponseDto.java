package com.sideProject.PlanIT.domain.post.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BannerResponseDto {
    private String title;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private String imagePath;

    @Builder
    public BannerResponseDto(String title, LocalDateTime startAt, LocalDateTime endAt, String imagePath) {
        this.title = title;
        this.startAt = startAt;
        this.endAt = endAt;
        this.imagePath = imagePath;
    }
}

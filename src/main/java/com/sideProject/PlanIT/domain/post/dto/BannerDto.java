package com.sideProject.PlanIT.domain.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

public class BannerDto {
    @Getter
    @AllArgsConstructor
    public static class BannerRequestDto {
        private String title;
        private LocalDateTime startAt;
        private LocalDateTime endAt;
        private MultipartFile image;
    }

    @Builder
    @Getter
    public static class BannerResponseDto {
        private String title;
        private LocalDateTime startAt;
        private LocalDateTime endAt;
        private String imagePath;
    }
}

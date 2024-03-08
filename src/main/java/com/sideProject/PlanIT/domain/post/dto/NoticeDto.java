package com.sideProject.PlanIT.domain.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

public class NoticeDto {
    @Getter
    @AllArgsConstructor
    public static class NoticeRequestDto {
        private String title;
        private LocalDateTime startAt;
        private LocalDateTime endAt;
        private MultipartFile attachment;
        private MultipartFile image;
        private String content;
    }

    @Getter
    @Builder
    public static class NoticeResponseDto {
        private String title;
        private LocalDateTime startAt;
        private LocalDateTime endAt;
        private String attachmentPath;
        private String imagePath;
        private String content;
    }
}

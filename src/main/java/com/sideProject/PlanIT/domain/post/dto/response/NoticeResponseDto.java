package com.sideProject.PlanIT.domain.post.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class NoticeResponseDto {
    private String title;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private String attachmentPath;
    private String imagePath;
    private String content;

    @Builder
    public NoticeResponseDto(String title, LocalDateTime startAt, LocalDateTime endAt, String attachmentPath, String imagePath, String content) {
        this.title = title;
        this.startAt = startAt;
        this.endAt = endAt;
        this.attachmentPath = attachmentPath;
        this.imagePath = imagePath;
        this.content = content;
    }
}

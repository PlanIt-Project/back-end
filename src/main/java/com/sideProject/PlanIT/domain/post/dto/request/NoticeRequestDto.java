package com.sideProject.PlanIT.domain.post.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NoticeRequestDto {
    private String title;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private MultipartFile attachment;
    private MultipartFile image;
    private String content;

    public NoticeRequestDto(String title, LocalDateTime startAt, LocalDateTime endAt, MultipartFile attachment, MultipartFile image, String content) {
        this.title = title;
        this.startAt = startAt;
        this.endAt = endAt;
        this.attachment = attachment;
        this.image = image;
        this.content = content;
    }
}

package com.sideProject.PlanIT.domain.post.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NoticeRequestDto {
    private String title;
    private LocalDate startAt;
    private LocalDate endAt;
    private MultipartFile attachment;
    private MultipartFile image;
    private String content;

    public NoticeRequestDto(String title, LocalDate startAt, LocalDate endAt, MultipartFile attachment, MultipartFile image, String content) {
        this.title = title;
        this.startAt = startAt;
        this.endAt = endAt;
        this.attachment = attachment;
        this.image = image;
        this.content = content;
    }
}

package com.sideProject.PlanIT.domain.post.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class NoticeRequestDto {
    private String title;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private MultipartFile attachment;
    private MultipartFile image;
    private String content;
}

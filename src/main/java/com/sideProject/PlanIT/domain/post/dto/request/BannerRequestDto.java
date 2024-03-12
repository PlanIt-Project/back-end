package com.sideProject.PlanIT.domain.post.dto.request;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
public class BannerRequestDto {
    private String title;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private MultipartFile image;
}

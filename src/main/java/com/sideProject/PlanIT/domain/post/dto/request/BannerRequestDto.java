package com.sideProject.PlanIT.domain.post.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BannerRequestDto {
    private String title;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private MultipartFile image;

    public BannerRequestDto(String title, LocalDateTime startAt, LocalDateTime endAt, MultipartFile image) {
        this.title = title;
        this.startAt = startAt;
        this.endAt = endAt;
        this.image = image;
    }
}

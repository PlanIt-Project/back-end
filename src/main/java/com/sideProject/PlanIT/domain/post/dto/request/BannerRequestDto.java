package com.sideProject.PlanIT.domain.post.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BannerRequestDto {
    @NotNull(message = "제목이 없습니다.")
    private String title;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startAt;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endAt;
    private MultipartFile image;

    @Builder
    public BannerRequestDto(String title, LocalDate startAt, LocalDate endAt, MultipartFile image) {
        this.title = title;
        this.startAt = startAt;
        this.endAt = endAt;
        this.image = image;
    }
}

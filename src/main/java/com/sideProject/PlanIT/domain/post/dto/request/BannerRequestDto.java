package com.sideProject.PlanIT.domain.post.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
public class BannerRequestDto {
    private String title;
    private LocalDate startAt;
    private LocalDate endAt;
    private MultipartFile image;

}

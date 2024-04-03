package com.sideProject.PlanIT.domain.post.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Builder
public class BannerRequestDto implements Serializable {
    private String title;
    private LocalDate startAt;
    private LocalDate endAt;
    private MultipartFile image;
}

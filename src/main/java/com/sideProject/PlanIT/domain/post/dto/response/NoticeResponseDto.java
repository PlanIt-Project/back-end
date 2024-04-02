package com.sideProject.PlanIT.domain.post.dto.response;

import com.sideProject.PlanIT.domain.post.entity.Notice;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NoticeResponseDto {
    private Long id;
    private String title;
    private LocalDate startAt;
    private LocalDate endAt;
    private String attachmentPath;
    private String imagePath;
    private String content;

    @Builder
    public NoticeResponseDto(Long id, String title, LocalDate startAt, LocalDate endAt, String attachmentPath, String imagePath, String content) {
        this.id = id;
        this.title = title;
        this.startAt = startAt;
        this.endAt = endAt;
        this.attachmentPath = attachmentPath;
        this.imagePath = imagePath;
        this.content = content;
    }

    public static NoticeResponseDto of(Notice notice) {
        return NoticeResponseDto.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .startAt(notice.getStartAt())
                .endAt(notice.getEndAt())
                .attachmentPath(notice.getAttachmentPath())
                .imagePath(notice.getImagePath())
                .content(notice.getContent())
                .build();
    }
}

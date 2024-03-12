package com.sideProject.PlanIT.domain.post.entity;

import com.sideProject.PlanIT.domain.post.dto.request.NoticeRequestDto;
import com.sideProject.PlanIT.domain.post.dto.response.NoticeResponseDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long Id;

    @Column
    private String title;

    @Column
    private LocalDateTime startAt;

    @Column
    private LocalDateTime endAt;

    @Column
    private String attachmentPath;

    @Column
    private String imagePath;

    @Column
    private String content;

    @Builder
    public Notice(String title, LocalDateTime startAt, LocalDateTime endAt, String attachmentPath, String imagePath, String content) {
        this.title = title;
        this.startAt = startAt;
        this.endAt = endAt;
        this.attachmentPath = attachmentPath;
        this.imagePath = imagePath;
        this.content = content;
    }

    public Notice update(NoticeRequestDto noticeRequestDto) {
        this.title = noticeRequestDto.getTitle();
        this.startAt = noticeRequestDto.getStartAt();
        this.endAt = noticeRequestDto.getEndAt();
        if (noticeRequestDto.getImage() != null) {
            this.imagePath = noticeRequestDto.getImage().getOriginalFilename();
        }
        if (noticeRequestDto.getAttachment() != null) {
            this.attachmentPath = noticeRequestDto.getAttachment().getOriginalFilename();
        }
        this.content = noticeRequestDto.getContent();

        return this;
    }

    public static NoticeResponseDto toDto(Notice notice) {
        return NoticeResponseDto.builder()
                .title(notice.title)
                .startAt(notice.startAt)
                .endAt(notice.endAt)
                .attachmentPath(notice.attachmentPath)
                .imagePath(notice.imagePath)
                .content(notice.content)
                .build();
    }
}

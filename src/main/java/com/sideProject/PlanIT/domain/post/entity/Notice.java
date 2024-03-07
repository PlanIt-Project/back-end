package com.sideProject.PlanIT.domain.post.entity;

import com.sideProject.PlanIT.domain.post.dto.NoticeDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@Builder
@NoArgsConstructor
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

    public Notice update(NoticeDto.NoticeRequestDto noticeRequestDto) {
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

    public static NoticeDto.NoticeResponseDto toDto(Notice notice) {
        return NoticeDto.NoticeResponseDto.builder()
                .title(notice.title)
                .startAt(notice.startAt)
                .endAt(notice.endAt)
                .attachmentPath(notice.attachmentPath)
                .imagePath(notice.imagePath)
                .content(notice.content)
                .build();
    }
}

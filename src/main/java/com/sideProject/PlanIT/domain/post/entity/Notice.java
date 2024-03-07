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
        this.imagePath = noticeRequestDto.getImage().getOriginalFilename();
        this.attachmentPath = noticeRequestDto.getAttachment().getOriginalFilename();
        this.content = noticeRequestDto.getContent();

        return this;
    }
}

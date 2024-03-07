package com.sideProject.PlanIT.domain.post.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Banner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long Id;

    @Column
    private LocalDateTime startAt;

    @Column
    //todo: 용도 다시 확인!
    private boolean isExposure;

    @Column
    //todo: 이미지 파일 시스템이 저장 or DB 저장 or 외부 스토리지 사용 중에서 선별 (회의)
    private String path;
}

package com.sideProject.PlanIT.domain.post.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
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
    private String imangePath;

    @Column
    private String content;
}

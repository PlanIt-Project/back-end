package com.sideProject.PlanIT.domain.post.controller;


import com.sideProject.PlanIT.common.response.ApiResponse;
import com.sideProject.PlanIT.domain.post.dto.request.NoticeRequestDto;
import com.sideProject.PlanIT.domain.post.dto.response.NoticeResponseDto;
import com.sideProject.PlanIT.domain.post.entity.Notice;
import com.sideProject.PlanIT.domain.post.service.NoticeService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class NoticeController {
    private final NoticeService noticeService;

    @PostMapping("/admin/notice")
    public ApiResponse<Notice> createNotice(@ModelAttribute NoticeRequestDto noticeRequestDto) {
        return ApiResponse.ok(noticeService.createNotice(noticeRequestDto));
    }

    @PutMapping("/admin/notice/{notice_id}")
    public ApiResponse<Notice> editNotice(@PathVariable Long notice_id , @ModelAttribute NoticeRequestDto noticeRequestDto) {
        return ApiResponse.ok(noticeService.editNotice(notice_id, noticeRequestDto));
    }

    @GetMapping("/admin/notice")
    public ApiResponse<List<NoticeResponseDto>> findAllNotices() {
        return ApiResponse.ok(noticeService.findAllNotices());
    }

    @GetMapping("/notice")
    public ApiResponse<List<NoticeResponseDto>> findAllNoticesInTime() {
        return ApiResponse.ok(noticeService.findAllNoticesInTime());
    }

    @GetMapping("/notice/{notice_id}")
    public ApiResponse<NoticeResponseDto> findNotice(Long notice_id) {
        return ApiResponse.ok(noticeService.findNotice(notice_id));
    }
}

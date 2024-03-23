package com.sideProject.PlanIT.domain.post.controller;

import com.sideProject.PlanIT.common.response.ApiResponse;
import com.sideProject.PlanIT.domain.post.dto.request.NoticeRequestDto;
import com.sideProject.PlanIT.domain.post.dto.response.NoticeResponseDto;
import com.sideProject.PlanIT.domain.post.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/notice")
@RequiredArgsConstructor
public class NoticeAdminController {
    private final NoticeService noticeService;

    @PostMapping
    public ApiResponse<String> createNotice(@ModelAttribute NoticeRequestDto noticeRequestDto) {
        return ApiResponse.ok(noticeService.createNotice(noticeRequestDto));
    }

    @PutMapping("/{notice_id}")
    public ApiResponse<String> editNotice(@PathVariable Long notice_id , @ModelAttribute NoticeRequestDto noticeRequestDto) {
        return ApiResponse.ok(noticeService.editNotice(notice_id, noticeRequestDto));
    }

    @DeleteMapping("/{notice_id}")
    public ApiResponse<String> deleteNotice(@PathVariable Long notice_id) {
        return ApiResponse.ok(noticeService.deleteNotice(notice_id));
    }

    @GetMapping
    public ApiResponse<List<NoticeResponseDto>> findAllNotices() {
        return ApiResponse.ok(noticeService.findAllNotices());
    }
}

package com.sideProject.PlanIT.domain.post.controller;

import com.sideProject.PlanIT.common.response.ApiResponse;
import com.sideProject.PlanIT.domain.post.dto.request.NoticeRequestDto;
import com.sideProject.PlanIT.domain.post.dto.response.NoticeResponseDto;
import com.sideProject.PlanIT.domain.post.service.NoticeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/notice")
@RequiredArgsConstructor
public class NoticeAdminController {
    private final NoticeService noticeService;

    @PostMapping
    public ApiResponse<String> createNotice(@Valid @ModelAttribute NoticeRequestDto request) {
        return ApiResponse.ok(noticeService.createNotice(request));
    }

    @PutMapping("/{notice_id}")
    public ApiResponse<String> editNotice(@PathVariable Long notice_id , @ModelAttribute NoticeRequestDto request) {
        return ApiResponse.ok(noticeService.editNotice(notice_id, request));
    }

    @DeleteMapping("/{notice_id}")
    public ApiResponse<String> deleteNotice(@PathVariable Long notice_id) {
        return ApiResponse.ok(noticeService.deleteNotice(notice_id));
    }

    @GetMapping
    public ApiResponse<Page<NoticeResponseDto>> findAllNotices(
            @PageableDefault(size = 10) Pageable pageable
    ) {
        return ApiResponse.ok(noticeService.findAllNotices(pageable));
    }
}

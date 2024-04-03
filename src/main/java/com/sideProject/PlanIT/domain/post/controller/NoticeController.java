package com.sideProject.PlanIT.domain.post.controller;


import com.sideProject.PlanIT.common.response.ApiResponse;
import com.sideProject.PlanIT.domain.post.dto.response.NoticeResponseDto;
import com.sideProject.PlanIT.domain.post.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notice")
public class NoticeController {
    private final NoticeService noticeService;

    @GetMapping
    public ApiResponse<Page<NoticeResponseDto>> findAllNoticesInTime(
            @PageableDefault(size = 10) Pageable pageable
    ) {
        return ApiResponse.ok(noticeService.findAllNoticesInTime(pageable));
    }

    @GetMapping("/{notice_id}")
    public ApiResponse<NoticeResponseDto> findNotice(@PathVariable Long notice_id) {
        return ApiResponse.ok(noticeService.findNotice(notice_id));
    }
}

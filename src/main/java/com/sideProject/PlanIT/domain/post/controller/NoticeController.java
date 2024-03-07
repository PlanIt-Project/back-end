package com.sideProject.PlanIT.domain.post.controller;


import com.sideProject.PlanIT.common.response.ApiResponse;
import com.sideProject.PlanIT.domain.post.dto.NoticeDto;
import com.sideProject.PlanIT.domain.post.entity.Notice;
import com.sideProject.PlanIT.domain.post.service.NoticeService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class NoticeController {
    private final NoticeService noticeService;

    @PostMapping("/admin/notice")
    public ApiResponse<Notice> createNotice(@ModelAttribute NoticeDto.NoticeRequestDto noticeRequestDto) {
        return ApiResponse.ok(noticeService.createNotice(noticeRequestDto));
    }

    @PutMapping("/admin/notice/{notice_id}")
    public ApiResponse<Notice> editNotice(@PathVariable Long notice_id , @ModelAttribute NoticeDto.NoticeRequestDto noticeRequestDto) {
        return ApiResponse.ok(noticeService.editNotice(notice_id, noticeRequestDto));
    }
}

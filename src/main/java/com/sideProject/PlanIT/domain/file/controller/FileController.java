package com.sideProject.PlanIT.domain.file.controller;

import com.sideProject.PlanIT.common.response.ApiResponse;
import com.sideProject.PlanIT.domain.file.service.FileService;
import com.sideProject.PlanIT.domain.post.dto.request.NoticeRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/file")
public class FileController {
    @Autowired
    FileService fileService;

    @PostMapping
    public ApiResponse<String> saveFile(@ModelAttribute NoticeRequestDto noticeRequestDto) {
        return ApiResponse.ok(fileService.saveFile(noticeRequestDto.getImage()));
    }
    @GetMapping("/{file_name:.+}")
    public ResponseEntity<?> sendFile(@PathVariable String file_name, HttpServletRequest request) {
        Resource resource = fileService.sendFile(file_name);

        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }
}

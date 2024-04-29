package com.sideProject.PlanIT.domain.health;

import com.sideProject.PlanIT.common.response.ApiResponse;
import com.sideProject.PlanIT.common.response.CustomException;
import com.sideProject.PlanIT.common.response.ErrorCode;
import com.sideProject.PlanIT.domain.post.dto.request.BannerRequestDto;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
public class HealthController {
    @GetMapping("")
    public ApiResponse<String> healthCheck() {
        return ApiResponse.ok("서버가 정상적으로 동작 중 입니다.");
    }
}

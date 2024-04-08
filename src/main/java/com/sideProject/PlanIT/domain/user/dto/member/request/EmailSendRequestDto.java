package com.sideProject.PlanIT.domain.user.dto.member.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailSendRequestDto {
    @Email
    @NotEmpty
    private String email;
}

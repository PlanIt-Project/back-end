package com.sideProject.PlanIT.domain.user.dto.member.request;

import jakarta.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailSendReqeustDto {
    @Email
    private String email;
}

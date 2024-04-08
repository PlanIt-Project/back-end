package com.sideProject.PlanIT.domain.user.dto.member.request;

import com.sideProject.PlanIT.domain.user.entity.enums.Gender;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberSignUpRequestDto {
    @NotNull(message = "이메일이 없습니다.")
    private String email;
    @NotNull(message = "비밀번호가 없습니다.")
    private String password;
    @NotNull(message = "이름이 없습니다.")
    private String name;
    @NotNull(message = "전화번호가 없습니다.")
    private String phone_number;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birth;
    @NotNull(message = "주소가 없습니다.")
    private String address;
    @NotNull(message = "성별이 없습니다.")
    private Gender gender;

    @Builder
    public MemberSignUpRequestDto(
            String email,
            String password,
            String name,
            String phone_number,
            LocalDate birth,
            String address,
            Gender gender
    ) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone_number = phone_number;
        this.birth = birth;
        this.address = address;
        this.gender = gender;
    }
}

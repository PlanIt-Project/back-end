package com.sideProject.PlanIT.domain.user.entity;

import com.sideProject.PlanIT.domain.user.dto.member.request.MemberEditRequestDto;
import com.sideProject.PlanIT.domain.user.entity.enums.Gender;
import com.sideProject.PlanIT.domain.user.entity.enums.MemberRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;


public class MemberTest {

    private Member initMember(String email, String password, String name) {
        return Member.builder()
                .email(email)
                .password(password)
                .name(name)
                .birth(LocalDate.of(2000, 1, 1))
                .phone_number("01012345678")
                .address("서울시")
                .gender(Gender.MALE)
                .role(MemberRole.MEMBER)
                .build();
    }

    @Nested
    @DisplayName("updateTest")
    public class updateTest {

        @DisplayName("Member의 정보를 수정한다")
        @Test
        public void update() {

            // given
            Member member = initMember("test@naver.com", "test1234", "test");
            MemberEditRequestDto memberEditRequestDto = MemberEditRequestDto.builder()
                    .name("test1")
                    .phone_number("01000000000")
                    .birth(LocalDate.of(2001, 2, 2))
                    .address("경기도")
                    .build();

            // when
            member.update(memberEditRequestDto);

            // then
            assertThat(member.getEmail()).isEqualTo("test@naver.com");
            assertThat(member.getName()).isEqualTo("test1");
            assertThat(member.getPhone_number()).isEqualTo("01000000000");
            assertThat(member.getBirth()).isEqualTo(LocalDate.of(2001, 2, 2));
            assertThat(member.getAddress()).isEqualTo("경기도");
        }
    }

    @Nested
    @DisplayName("changePasswordTest")
    public class changePasswordTest {

        @DisplayName("비밀번호를 변경한다")
        @Test
        public void changePassword() {

            // given
            Member member = initMember("test@naver.com", "test1234", "test");
            String newPassword = "test1111";

            // when
            member.changePassword(newPassword);

            // then
            assertThat(member.getEmail()).isEqualTo("test@naver.com");
            assertThat(member.getPassword()).isEqualTo("test1111");
        }
    }

    @Nested
    @DisplayName("grantEmployeeAuthTest")
    public class grantEmployeeAuthTest {

        @DisplayName("Member의 권한을 TRAINER로 변경한다")
        @Test
        public void grantEmployeeAuth() {

            // given
            Member member = initMember("test@naver.com", "test1234", "test");

            // when
            member.grantEmployeeAuth();

            // then
            assertThat(member.getEmail()).isEqualTo("test@naver.com");
            assertThat(member.getRole()).isEqualTo(MemberRole.TRAINER);
        }
    }


}
